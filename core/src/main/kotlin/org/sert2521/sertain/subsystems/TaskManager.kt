package org.sert2521.sertain.subsystems

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
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

fun CoroutineScope.manageTasks() {
    subscribe<Use<Any?>> { use ->
        // Subsystems used in parent coroutine
        val prevWorkers: Set<Worker<*>> = use.context[Requirements] ?: emptySet()
        // Subsystems used in new coroutine but not in parent coroutine
        val newWorkers = use.workers - prevWorkers
        // Subsystems from both parent and new coroutines
        val allWorkers = use.workers + prevWorkers

        if (allWorkers.any { !it.isEnabled }) {
            use.continuation.resumeWithException(
                    CancellationException(
                            "Cannot execute action ${use.name} because the following workers are disabled:" +
                                    " ${allWorkers.filter { !it.isEnabled }.joinToString(" ")}."
                    )
            )
        }

        // Workers that are already occupied
        val occupiedWorkers = newWorkers.filter { it.occupied }

        if (!use.cancelConflicts && occupiedWorkers.isNotEmpty()) {
            use.continuation.resumeWithException(
                    CancellationException(
                            "Cannot execute low priority action ${use.name} because the following workers are " +
                                    "occupied: ${occupiedWorkers.joinToString(" ")}"
                    )
            )
        }

        // Jobs of conflicting workers
        val conflictingJobs = occupiedWorkers.map { it.currentJob!! }.filter { !it.isCompleted }.toSet()

        val newJob = CoroutineScope(use.context).launch(
                Requirements(allWorkers),
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
                RobotScope.fire(Clean(newWorkers, coroutineContext[Job]!!))
            }
        }

        newWorkers.forEach { it.currentJob = newJob }
    }

    subscribe<Clean> { clean ->
        clean.workers
                .filter { it.currentJob == clean.job }
                .forEach {
                    (it as Worker<Any?>).let { s ->
                        s.currentJob = null
                        s.defaultIfNotNull()
                    }
                }
    }
}

private class Requirements(
    requirements: Set<Worker<*>>
) : Set<Worker<*>> by requirements, AbstractCoroutineContextElement(Key) {
    companion object Key : CoroutineContext.Key<Requirements>
}
