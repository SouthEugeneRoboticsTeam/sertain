package org.sert2521.sertain.subsystems

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import org.sert2521.sertain.coroutines.RobotScope

open class Worker<T>(
        val value: T,
        val name: String = "Anonymous Subsystem",
        open var isEnabled: Boolean = true,
        open val default: (suspend CoroutineScope.(s: T) -> Any?)? = null,
        open val setup: (CoroutineScope.(s: T) -> Any?)? = null
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

fun <T> worker(value: T, name: String = "Unnamed Subsystem", configure: WorkerConfig<T>.() -> Unit = {}): Worker<T> {
    val config = WorkerConfig<T>().apply(configure)
    val w = Worker(value, name, config.isEnabled, config.default, config.setup)
    Workers.add(w)
    return w
}
