package org.sert2521.sertain.subsystems

import kotlinx.coroutines.CoroutineScope

typealias Default = suspend CoroutineScope.(s: Any?) -> Unit
typealias Setup = CoroutineScope.(s: Any?) -> Unit

abstract class Subsystem(val name: String? = null, var enabled: Boolean = true) {
    open val default: Default? = null

    open val setup: Setup? = null
}
