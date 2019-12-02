package org.sert2521.sertain.subsystems

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.sert2521.sertain.coroutines.RobotScope
import org.sert2521.sertain.events.Clean
import org.sert2521.sertain.events.Use
import org.sert2521.sertain.events.fire
import org.sert2521.sertain.events.subscribe
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

@ExperimentalCoroutinesApi
fun CoroutineScope.manageSubsystems() {
    subscribe<Use<Any?>> { use ->
        // Subsystems used in parent coroutine
        val prevSubsystems: Set<Subsystem> = use.context[Requirements] ?: emptySet()
        // Subsystems used in new coroutine but not in parent coroutine
        val newSubsystems = use.subsystems - prevSubsystems
        // Subsystems from both parent and new coroutines
        val allSubsystems = use.subsystems + prevSubsystems

        if (allSubsystems.any { !it.isEnabled }) {
            use.continuation.resumeWithException(
                    CancellationException(
                            "Cannot execute action ${use.name} because the following subsystems are disabled:" +
                                    " ${allSubsystems.filter { !it.isEnabled }.joinToString(" ")}."
                    )
            )
        }

        // Subsystems that are already occupied
        val occupiedSubsystems = allSubsystems.filter { it.occupied }

        if (!use.cancelConflicts && occupiedSubsystems.isNotEmpty()) {
            use.continuation.resumeWithException(
                    CancellationException(
                            "Cannot execute unimportant action ${use.name} because the following subsystems are " +
                                    "occupied: ${occupiedSubsystems.joinToString(" ")}"
                    )
            )
        }

        // Jobs of conflicting subsystems
        val conflictingJobs = occupiedSubsystems.map { it.currentJob!! }

        val newJob = CoroutineScope(use.context).launch(
                Requirements(allSubsystems),
                CoroutineStart.ATOMIC
        ) {
            try {
                // Suspend until all conflicting jobs are canceled and joined
                withContext(NonCancellable) {
                    conflictingJobs.forEach { it.cancel() }
                    conflictingJobs.forEach { it.join() }
                }

                val result = coroutineScope { use.action(this) }

                use.continuation.resume(result)
            } catch (e: Throwable) {
                use.continuation.resumeWithException(e)
            } finally {
                fire(Clean(newSubsystems, coroutineContext[Job]!!))
            }
        }

        newSubsystems.forEach { it.currentJob = newJob }
    }

    subscribe<Clean> { clean ->
        clean.subsystems
                .filter { it.currentJob == clean.job }
                .forEach {
                    it.currentJob = null
                    if (it.default != null) {
                        RobotScope.launch {
                            use(it, name = "DEFAULT") { it.default.invoke() }
                        }
                    }
                }
    }
}

private class Requirements(
    requirements: Set<Subsystem>
) : Set<Subsystem> by requirements, AbstractCoroutineContextElement(Key) {
    companion object Key : CoroutineContext.Key<Requirements>
}

internal var subsystems = mutableMapOf<KClass<*>, Subsystem>()

fun <S : Subsystem> add(reference: KClass<*>, subsystem: S) {
    subsystems[reference] = subsystem
}

fun <S : Subsystem> add(subsystem: S) {
    add(subsystem::class, subsystem)
}

inline fun <reified S : Subsystem> add() {
    add(S::class, S::class.createInstance())
}

fun <S : Subsystem> access(reference: KClass<S>): S {
    @Suppress("unchecked_cast") // Safe because subsystems is internally managed
    subsystems[reference]?.let {
        return it as S
    }
    throw IllegalStateException("Subsystem with type ${reference.qualifiedName} does not exist." +
            " Did you forget to add it?")
}

inline fun <reified S : Subsystem> access() = access(S::class)

inline fun <reified S : Subsystem> TaskConfigure.use(): S {
    val subsystem = access(S::class)
    this += subsystem
    return subsystem
}
