package org.sert2521.sertain.subsystems

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import org.sert2521.sertain.coroutines.RobotScope
import org.sert2521.sertain.events.Use
import org.sert2521.sertain.events.fire
import kotlin.coroutines.coroutineContext

abstract class Subsystem(val name: String, val default: (suspend () -> Unit)? = null) {
    var isEnabled = true

    internal var currentJob: Job? = null

    val occupied: Boolean
        get() = currentJob != null

    init {
        RobotScope.launch {

        }
    }
}
