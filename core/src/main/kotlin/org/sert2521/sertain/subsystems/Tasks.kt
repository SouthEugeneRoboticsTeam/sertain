package org.sert2521.sertain.subsystems

import kotlinx.coroutines.CoroutineScope
import org.sert2521.sertain.coroutines.RobotDispatcher
import kotlin.coroutines.CoroutineContext

@DslMarker
annotation class TaskDsl

@TaskDsl
class TaskConfigure {
    internal val subsystems = mutableListOf<Subsystem>()

    operator fun plusAssign(subsystem: Subsystem) {
        subsystems += subsystem
    }

    internal var action: (suspend ActionConfigure.() -> Unit)? = null

    fun action(action: suspend ActionConfigure.() -> Unit) {
        this.action = action
    }
}

@TaskDsl
class ActionConfigure : CoroutineScope {
    override val coroutineContext: CoroutineContext = RobotDispatcher
}

suspend fun doTask(name: String = "ANONYMOUS_TASK", configure: TaskConfigure.() -> Unit) {
    with(TaskConfigure().apply(configure)) {
        action?.let {
                use(*subsystems.toTypedArray(), action = (it as suspend CoroutineScope.() -> Unit))
        }
    }
}

val <S : Subsystem> S.accessor: TaskConfigure.() -> S
        get() = {
            this += this@accessor
            this@accessor
        }
