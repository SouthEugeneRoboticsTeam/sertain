package org.sert2521.sertain.eventies

import kotlinx.coroutines.CoroutineScope
import kotlin.math.abs
import kotlin.reflect.KClass

interface Sub<E : Event> {
    val action: suspend CoroutineScope.(E) -> Unit
    fun requires(e: Event): Boolean
    fun <E : Event> withEventOrNull(e: E): Sub<E>? {
        return if (requires(e)) this as Sub<E> else null
    }
}
