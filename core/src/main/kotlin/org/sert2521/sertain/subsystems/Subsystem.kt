package org.sert2521.sertain.subsystems

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import org.sert2521.sertain.coroutines.RobotScope
import org.sert2521.sertain.events.Use
import org.sert2521.sertain.events.fire

abstract class Subsystem {
    internal var currentJob: Job? = null
    val inUse: Boolean
        get() = currentJob?.isActive ?: false

    internal var default: (suspend () -> Unit)? = null

    fun default(action: suspend () -> Unit) {
        currentJob?.cancel()
        default = action
    }
}

suspend fun <R> use(vararg subsystems: Subsystem, important: Boolean = true, action: suspend CoroutineScope.() -> R): R {
    return suspendCancellableCoroutine { continuation ->
        RobotScope.launch {
            fire(Use(
                    subsystems.toSet(),
                    important,
                    coroutineContext,
                    continuation,
                    action
            ))
        }
    }
}
