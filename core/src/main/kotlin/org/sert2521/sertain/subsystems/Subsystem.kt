package org.sert2521.sertain.subsystems

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import org.sert2521.sertain.events.Use
import org.sert2521.sertain.events.fire
import kotlin.coroutines.coroutineContext

abstract class Subsystem {
    var isEnabled = true

    internal var currentJob: Job? = null

    val occupied: Boolean
        get() = currentJob != null

    internal var default: (suspend CoroutineScope.() -> Unit)? = null

    fun default(action: suspend CoroutineScope.() -> Unit) {
        default = action
    }
}

suspend fun <R> use(vararg subsystems: Subsystem, important: Boolean = true, action: suspend CoroutineScope.() -> R): R {
    val context = coroutineContext
    return suspendCancellableCoroutine { continuation ->
        CoroutineScope(context).launch {
            fire(Use(
                    subsystems.toSet(),
                    important,
                    context,
                    continuation,
                    action
            ))
        }
    }
}
