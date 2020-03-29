package org.sert2521.sertain.subsystems

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.sert2521.sertain.coroutines.RobotScope
import org.sert2521.sertain.events.Start
import org.sert2521.sertain.events.subscribe
import kotlin.coroutines.coroutineContext

val workers = mutableListOf<Worker<*>>()

data class Worker<T>(
        val value: T,
        val name: String = "Anonymous Subsystem",
        var isEnabled: Boolean = true,
        val default: (suspend CoroutineScope.(s: T) -> Any?)? = null,
        val setup: (CoroutineScope.(s: T) -> Any?)? = null
) {
    internal var currentJob: Job? = null

    val occupied: Boolean
        get() = currentJob != null

    suspend operator fun <R> invoke(action: suspend CoroutineScope.(T) -> R) = use(this, action = action)
}

open class WorkerConfig<T> {
    var default: (suspend CoroutineScope.(s: T) -> Any?)? = null
    var defaultScope: CoroutineScope = RobotScope

    fun default(scope: CoroutineScope = RobotScope, action: suspend CoroutineScope.(s: T) -> Any?) {
        default = action
        defaultScope = scope
    }

    var setup: (CoroutineScope.(s: T) -> Any?)? = null

    fun setup(action: CoroutineScope.(s: T) -> Any?) {
        setup = action
    }

    var isEnabled = true
}

inline fun <T> worker(value: T, name: String = "Unnamed Subsystem", configure: WorkerConfig<T>.() -> Unit = {}): Worker<T> {
    val config = WorkerConfig<T>().apply(configure)
    val s = Worker(value, name, config.isEnabled, config.default, config.setup)
    s.setup?.invoke(RobotScope, s.value)
    if (s.default != null) config.defaultScope.subscribe<Start> {
        reserve(s, name = "Default") { s.default.invoke(this, s.value) }
    }
    workers += s
    return s
}

suspend fun <T> Worker<T>.defaultIfNotNull() {
    if (default != null) {
        CoroutineScope(coroutineContext).launch {
            reserve(this@defaultIfNotNull) {
                default.invoke(this, value)
            }
        }
    }
}
