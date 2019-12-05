package org.sert2521.sertain.subsystems

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.cancelAndJoin
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

fun CoroutineScope.manageTasks() {
    subscribe<Use<Any?>> { use ->
        println("processing task ${use.name}")
        // Subsystems used in parent coroutine
        val prevSubsystems: Set<Subsystem> = use.context[Requirements] ?: emptySet()
        // Subsystems used in new coroutine but not in parent coroutine
        val newSubsystems = use.subsystems - prevSubsystems
        // Subsystems from both parent and new coroutines
        val allSubsystems = use.subsystems + prevSubsystems

        allSubsystems.forEach {
            println("Using subsystem ${it.name}")
        }

        if (allSubsystems.any { !it.isEnabled }) {
            use.continuation.resumeWithException(
                    CancellationException(
                            "Cannot execute action ${use.name} because the following subsystems are disabled:" +
                                    " ${allSubsystems.filter { !it.isEnabled }.joinToString(" ")}."
                    )
            )
        }

        // Subsystems that are already occupied
        val occupiedSubsystems = newSubsystems.filter { it.occupied }

        if (!use.cancelConflicts && occupiedSubsystems.isNotEmpty()) {
            use.continuation.resumeWithException(
                    CancellationException(
                            "Cannot execute unimportant action ${use.name} because the following subsystems are " +
                                    "occupied: ${occupiedSubsystems.joinToString(" ")}"
                    )
            )
        }

        // Jobs of conflicting subsystems
        val conflictingJobs = occupiedSubsystems.map { it.currentJob!! }.toSet()

        println("Creating new job for ${use.name}")
        val newJob = CoroutineScope(use.context).launch(
                Requirements(allSubsystems),
                CoroutineStart.ATOMIC
        ) {
            println("Trying to run task ${use.name}")
            try {
                println("A'ight I'm boutta do it")
                // Suspend until all conflicting jobs are canceled and joined
                withContext(NonCancellable) {
                    println("Joining and cancelling ${conflictingJobs.size}")
                    conflictingJobs.forEach {
                        println("Cancelling job")
                        it.cancelAndJoin()
                    }
                    println("Canceled conflicting jobs")
                }

                println("About to run task ${use.name}")

                val result = coroutineScope {
                    println("Running task ${use.name}")
                    use.action(this)
                }

                use.continuation.resume(result)
            } catch (e: Throwable) {
                println(e)
                use.continuation.resumeWithException(e)
            } finally {
                println("Cleaning task ${use.name}")
                fire(Clean(newSubsystems, coroutineContext[Job]!!))
            }
        }

        newSubsystems.forEach { it.currentJob = newJob }
    }

    subscribe<Clean> { clean ->
        println("Cleaning subsystems ${clean.subsystems.size}")
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
