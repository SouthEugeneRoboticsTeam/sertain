package org.sert2521.sertain.subsystems

import kotlinx.coroutines.CoroutineScope

/**
 * A container for code that represents an independently functioning part of the robot.
 *
 * @property default Command to run after any other command finishes.
 * @property setup Code to run when subsystem is added using [add].
 */
abstract class Subsystem(val name: String? = null, var enabled: Boolean = true) {
    open suspend fun CoroutineScope.default() = Unit

    open fun CoroutineScope.setup() = Unit
}
