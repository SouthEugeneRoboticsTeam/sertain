package org.sert2521.sertain.subsystems

import edu.wpi.first.wpilibj.DriverStation.reportError
import kotlinx.coroutines.*
import org.sert2521.sertain.coroutines.RobotDispatcher
import org.sert2521.sertain.coroutines.RobotScope
import org.sert2521.sertain.events.Use
import org.sert2521.sertain.events.subscribe
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

fun manageSubsystems() {
    GlobalScope.launch(RobotDispatcher) {
        subscribe<Use<Any?>> { use ->
            // Subsystems used in parent coroutine
            val lastSubsystems: Set<Subsystem> = use.callerContext[Requirements] ?: emptySet()
            // Subsystems used in new coroutine but not in parent coroutine
            val newSubsystems = use.subsystems - lastSubsystems
            // Subsystems from both parent and new coroutines
            val subsystems = use.subsystems + lastSubsystems
            // Subsystems that are already in use
            val conflictingSubsystems = subsystems.filter { it.inUse }

            val newJob = CoroutineScope(use.callerContext).launch(
                    Requirements(subsystems),
                    CoroutineStart.ATOMIC
            ) {
                try {
                    // Suspend until all conflicting jobs are canceled and joined
                    withContext(NonCancellable) {
                        conflictingSubsystems.forEach { it.currentJob?.cancel() }
                        conflictingSubsystems.forEach { it.currentJob?.join() }
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
