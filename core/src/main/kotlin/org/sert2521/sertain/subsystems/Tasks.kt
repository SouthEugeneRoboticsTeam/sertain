package org.sert2521.sertain.subsystems

import kotlinx.coroutines.CoroutineScope

class TaskConfigure {
    internal val subsystems = mutableListOf<Subsystem>()

    operator fun plusAssign(subsystem: Subsystem) {
        subsystems += subsystem
    }

    internal var action: (suspend CoroutineScope.() -> Unit)? = null

    fun action(action: suspend CoroutineScope.() -> Unit) {
        this.action = action
    }
}

suspend fun doTask(configure: TaskConfigure.() -> Unit) {
    with(TaskConfigure().apply(configure)) {
        action?.let { action ->
            use(*subsystems.toTypedArray(), action = action)
        }
    }
}

val Subsystem.accessor: TaskConfigure.() -> Subsystem
        get() = {
            this += this@accessor
            this@accessor
        }
