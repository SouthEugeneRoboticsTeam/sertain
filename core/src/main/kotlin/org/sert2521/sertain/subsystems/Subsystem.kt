package org.sert2521.sertain.subsystems

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.sert2521.sertain.coroutines.RobotScope

abstract class Subsystem(val name: String, val default: (suspend () -> Unit)? = null) {
    var isEnabled = true

    internal var currentJob: Job? = null

    val occupied: Boolean
        get() = currentJob != null
}
