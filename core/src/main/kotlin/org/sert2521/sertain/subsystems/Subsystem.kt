package org.sert2521.sertain.subsystems

import kotlinx.coroutines.Job

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

fun clear(subsystem: Subsystem, important: Boolean = true): Boolean {
    if (important || !subsystem.inUse) {
        subsystem.currentJob?.cancel()
        return true
    }
    return false
}
