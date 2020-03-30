package org.sert2521.sertain.subsystems

import kotlinx.coroutines.CoroutineScope
import kotlin.reflect.full.createInstance

class SubsystemWorker<S : Subsystem>(subsystem: S) : Worker<S>(
        value = subsystem,
        name = subsystem.name ?: subsystem::class.simpleName ?: "Unnamed Subsystem"
) {
    override var isEnabled: Boolean
        get() = value.enabled
        set(value) {
            this.value.enabled = value
        }

    override val default: (suspend CoroutineScope.(s: S) -> Any?)?
        get() = value.default
    override val setup: (CoroutineScope.(s: S) -> Any?)?
        get() = value.setup
}

inline fun <reified S : Subsystem> add(): SubsystemWorker<S> {
    val w = SubsystemWorker(S::class.createInstance())
    Workers.add(w)
    return w
}
