package org.botful.subsystems

import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.sert2521.sertain.events.Event
import org.sert2521.sertain.events.Events
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

fun manageTasks() {
    Events.subscribe<Use<Any?>> { use ->
        // Subsystems used in parent coroutine
        val prevSubsystems: Set<Subsystem> = use.context[Requirements] ?: emptySet()
        // Subsystems used in new coroutine but not in parent coroutine
        val newSubsystems = use.subsystems - prevSubsystems
        // Subsystems from both parent and new coroutines
        val allSubsystems = use.subsystems + prevSubsystems

        if (allSubsystems.any { !it.isEnabled }) {
            use.continuation.resumeWithException(
                    CancellationException(
                            "Cannot execute action ${use.name} because the following workers are disabled:" +
                                    " ${allSubsystems.filter { !it.isEnabled }.joinToString(" ")}."
                    )
            )
        }

        // Workers that are already occupied
        val occupiedSubsystems = newSubsystems.filter { it.occupied }

        if (!use.cancelConflicts && occupiedSubsystems.isNotEmpty()) {
            use.continuation.resumeWithException(
                    CancellationException(
                            "Cannot execute low priority action ${use.name} because the following subsystems are " +
                                    "occupied: ${occupiedSubsystems.joinToString(" ")}"
                    )
            )
        }

        // Jobs of conflicting subsystems
        val conflictingJobs = occupiedSubsystems.map { it.job!! }.filter { !it.isCompleted }.toSet()

        val newJob = CoroutineScope(use.context).launch(
                Requirements(allSubsystems),
                CoroutineStart.ATOMIC
        ) {
            try {
                // Suspend until all conflicting jobs are canceled and joined
                withContext(NonCancellable) {
                    conflictingJobs.toSet().forEach {
                        it.cancel()
                    }
                    conflictingJobs.forEach {
                        it.join()
                    }
                }

                val result = coroutineScope { use.action(this) }

                use.continuation.resume(Result.success(result))
            } catch (e: Throwable) {
                use.continuation.resume(Result.failure(e))
            } finally {
                Events.fire(Clean(newSubsystems, coroutineContext[Job]!!))
            }
        }

        newSubsystems.forEach { it.job = newJob }
    }

    Events.subscribe<Clean> { clean ->
        clean.subsystems
                .filter { it.job == clean.job }
                .forEach {
                    it.job = null
                    launch {
                        reserve(it, cancelConflicts = false, name = "Default") {
                            it.apply { default() }
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

interface SubsystemEvent : Event

class Use<R>(
        val subsystems: Set<Subsystem>,
        val cancelConflicts: Boolean,
        val name: String,
        val context: CoroutineContext,
        val continuation: CancellableContinuation<Result<R>>,
        val action: suspend CoroutineScope.() -> R
) : SubsystemEvent

class Clean(
        val subsystems: Set<Subsystem>,
        val job: Job
) : SubsystemEvent
