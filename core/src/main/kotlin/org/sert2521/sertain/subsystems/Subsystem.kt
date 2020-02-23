package org.sert2521.sertain.subsystems

import kotlinx.coroutines.Job

abstract class Subsystem(val name: String = "Anonymous Subsystem", val default: (suspend () -> Unit)? = null) {
    var isEnabled = true

    internal var currentJob: Job? = null

    val occupied: Boolean
        get() = currentJob != null
}
