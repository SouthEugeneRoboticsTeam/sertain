package org.sert2521.sertain.eventies

import kotlinx.coroutines.CoroutineScope

interface Sub<E : Event> {
    val action: suspend CoroutineScope.(E) -> Unit
    fun requires(e: Event): Boolean
}
