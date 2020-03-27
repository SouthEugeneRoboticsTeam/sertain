package org.sert2521.sertain.subsystems

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.sert2521.sertain.coroutines.RobotScope

data class Subsystem<T>(
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

class SubsystemConfig<T> {
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

inline fun <reified T> subsystem(value: T, name: String = "Unnamed Subsystem", configure: SubsystemConfig<T>.() -> Unit): Subsystem<T> {
    val config = SubsystemConfig<T>().apply(configure)
    val s = Subsystem(value, name, config.isEnabled, config.default, config.setup)
    s.setup?.invoke(RobotScope, s.value)
    if (s.default != null) config.defaultScope.launch {
        reserve(s) { s.default.invoke(this, s.value) }
    }
    return Subsystem(
            value,
            name,
            config.isEnabled,
            config.default,
            config.setup
    )
}
