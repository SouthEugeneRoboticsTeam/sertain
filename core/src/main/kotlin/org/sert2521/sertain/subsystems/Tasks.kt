package org.sert2521.sertain.subsystems

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import org.sert2521.sertain.events.Use
import org.sert2521.sertain.events.fire
import kotlin.coroutines.coroutineContext

class TaskConfigure {
    internal val subsystems = mutableListOf<Subsystem>()

    operator fun plusAssign(subsystem: Subsystem) {
        subsystems += subsystem
    }

    internal var action: (suspend CoroutineScope.() -> Unit) = {}

    fun action(action: suspend CoroutineScope.() -> Unit) {
        this.action = action
    }
}

suspend fun doTask(name: String = "ANONYMOUS_TASK", configure: TaskConfigure.() -> Unit) {
    with(TaskConfigure().apply(configure)) {
        action.let {
            @Suppress("unchecked_cast") // Will work, ActionConfigure extends CoroutineScope
            (use(*subsystems.toTypedArray(), name = name, action = it))
        }
    }
}

suspend fun <R> use(
        vararg subsystems: Subsystem,
        cancelConflicts: Boolean = true,
        cancelDuplicates: Boolean = true,
        name: String = "ANONYMOUS_TASK",
        action: suspend CoroutineScope.() -> R
): R {
    val context = coroutineContext
    return suspendCancellableCoroutine { continuation ->
        CoroutineScope(context).launch {
            fire(Use(
                    subsystems.toSet(),
                    cancelConflicts,
                    name,
                    context,
                    continuation,
                    action
            ))
        }
    }
}
