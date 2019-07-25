package org.sert2521.sertain.subsystems

abstract class Subsystem {
    internal var default: (suspend () -> Unit)? = null

    fun default(action: suspend () -> Unit) {
        default = action
    }
}
