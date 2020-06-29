package org.sert2521.sertain.eventies

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import org.sert2521.sertain.coroutines.delayForever

fun fire(event: Event) = EventBus.fire(event)

inline fun <reified E : Event> CoroutineScope.subscribe(sub: Sub<E>): Job {
    return launch {
        try {
            EventBus.subscribe(sub)
            delayForever()
        } finally {
            EventBus.remove(sub)
        }
    }
}

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

inline fun <reified E : Event.Targeted<*>> CoroutineScope.subscribe(target: Any?, noinline action: suspend CoroutineScope.(E) -> Unit): Job {
    var sub: Sub<E>? = null
    return launch {
        try {
            sub = EventBus.subscribe(target, action)
            delayForever()
        } finally {
            EventBus.remove(sub!!)
        }
    }
}

inline fun <reified E1 : Event, reified E2 : Event> CoroutineScope.between(noinline action: suspend CoroutineScope.(E1) -> Unit): Job {
    var sub: Sub<E1>? = null
    return launch {
        try {
            sub = EventBus.between<E1, E2>(action)
            delayForever()
        } finally {
            EventBus.remove(sub!!)
        }
    }
}

inline fun <reified E1 : Event.Targeted<*>, reified E2 : Event.Targeted<*>> CoroutineScope.between(target: Any?, noinline action: suspend CoroutineScope.(E1) -> Unit): Job {
    var sub: Sub<E1>? = null
    return launch {
        try {
            sub = EventBus.between<E1, E2>(target, action)
            delayForever()
        } finally {
            EventBus.remove(sub!!)
        }
    }
}
