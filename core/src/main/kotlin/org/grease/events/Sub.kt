package org.grease.events

import kotlinx.coroutines.CoroutineScope

interface Sub<E : Event> {
    val action: suspend CoroutineScope.(E) -> Unit
    fun requires(e: Event): Boolean
}
