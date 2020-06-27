package org.sert2521.sertain.eventies

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import org.sert2521.sertain.coroutines.delayForever

inline fun <reified E : Event> CoroutineScope.subscribe(noinline action: suspend CoroutineScope.(E) -> Unit): Job {
    var sub: Sub<E>? = null
    return launch {
        try {
            sub = EventBus.subscribe(action)
            delayForever()
        } finally {
            EventBus.remove(sub!!)
        }
    }
}
