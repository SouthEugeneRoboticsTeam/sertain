package org.sert2521.sertain.eventies

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

@InternalCoroutinesApi
inline fun <reified E : Event> CoroutineScope.subscribe(noinline action: suspend CoroutineScope.(E) -> Unit) {
    launch {
        val sub = EventBus.subscribe(action)
        try {} finally {
            EventBus.remove(sub)
        }
    }
}
