package org.sert2521.sertain.subsystems

import kotlinx.coroutines.*
import org.sert2521.sertain.coroutines.RobotScope
import org.sert2521.sertain.events.Use
import org.sert2521.sertain.events.subscribe
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

fun manageSubsystems() {
    RobotScope.launch {
        subscribe<Use<Any?>> { use ->
            // Subsystems used in parent coroutine
            val lastSubsystems: Set<Subsystem> = use.callerContext[Requirements] ?: emptySet()
            // Subsystems used in new coroutine but not in parent coroutine
            val newSubsystems = use.subsystems - lastSubsystems
            // Subsystems from both parent and new coroutines
            val subsystems = use.subsystems + lastSubsystems
            // Subsystems that are already in use
            val conflictingSubsystems = subsystems.filter { it.inUse }

            val conflictingJobs = conflictingSubsystems.map { it.currentJob!! }

            val newJob = CoroutineScope(use.callerContext).launch(
                    Requirements(subsystems),
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
                }
            }

            newSubsystems.forEach { it.currentJob = newJob }
        }
    }
}

private class Requirements(
        requirements: Set<Subsystem>
) : Set<Subsystem> by requirements, AbstractCoroutineContextElement(Key) {
    companion object Key : CoroutineContext.Key<Requirements>
}
