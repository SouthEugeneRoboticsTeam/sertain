package org.sertain.subsystems

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * A container for code that represents an independently functioning part of the robot.
 *
 * @property default Command to run after any other command finishes.
 * @property setup Code to run when subsystem is added using [add].
 */
abstract class Subsystem(val name: String? = null, var isEnabled: Boolean = true) {
    internal var job: Job? = null
    val occupied get() = job != null

    open suspend fun CoroutineScope.default() = Unit

    open fun CoroutineScope.setup() = Unit
}

class Key<S : Subsystem> internal constructor()

fun CoroutineScope.add(vararg subsystems: Subsystem) {
    for (s in subsystems) {
        s.apply { setup() }
    }
    for (s in subsystems) {
        launch {
            reserve(s, cancelConflicts = false, name = "Default") { s.apply { default() } }
        }
    }
}
