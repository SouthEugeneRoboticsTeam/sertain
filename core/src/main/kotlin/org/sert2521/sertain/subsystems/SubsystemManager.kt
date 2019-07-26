package org.sert2521.sertain.subsystems

import kotlinx.coroutines.launch
import org.sert2521.sertain.coroutines.RobotScope
import org.sert2521.sertain.events.TaskEvent
import org.sert2521.sertain.events.Use
import org.sert2521.sertain.events.subscribe

val uses = listOf<Use>()

fun RobotScope.manageSubsystems() {
    launch {
        subscribe<Use> { use ->
            // Clean up current jobs
            use.subsystems.forEach {
                if (it.inUse && use.important) {
                    it.currentJob?.cancel()
                }
            }
            val newJob = RobotScope.launch { use.action() }
            use.subsystems.forEach {
                it.currentJob = newJob
            }
        }
    }
}
