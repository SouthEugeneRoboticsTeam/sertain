package org.sert2521.sertain.subsystems

import kotlinx.coroutines.CoroutineScope

abstract class Subsystem(val name: String? = null, var enabled: Boolean = true) {
    open suspend fun CoroutineScope.default() = Unit

    open fun CoroutineScope.setup() = Unit
}
