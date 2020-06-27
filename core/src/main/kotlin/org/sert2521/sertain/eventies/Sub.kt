package org.sert2521.sertain.eventies

import kotlinx.coroutines.CoroutineScope
import kotlin.reflect.KClass

class Sub<E : Event>(
        val kClass: KClass<E>,
        val action: suspend CoroutineScope.(E) -> Unit
) {
    inline fun <reified E : Event> eventIs() = E::class == kClass
}
