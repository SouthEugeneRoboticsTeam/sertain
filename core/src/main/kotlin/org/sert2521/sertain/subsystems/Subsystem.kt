package org.sert2521.sertain.subsystems

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
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
}

class SubsystemConfig<T> {
    var default: (suspend CoroutineScope.(s: T) -> Any?)? = null

    var setup: (CoroutineScope.(s: T) -> Any?)? = null

    var isEnabled = true
}

inline fun <T> subsystem(value: T, name: String = "Unnamed Subsystem", configure: SubsystemConfig<T>.() -> Unit): Subsystem<T> {
    val config = SubsystemConfig<T>().apply(configure)
    val s = Subsystem(value, name, config.isEnabled, config.default, config.setup)
    s.setup?.invoke(RobotScope, s.value)

}
