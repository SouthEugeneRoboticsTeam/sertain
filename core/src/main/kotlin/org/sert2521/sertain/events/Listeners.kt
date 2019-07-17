package org.sert2521.sertain.events

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

val listeners = mutableMapOf<String, MutableList<suspend (Event) -> Unit>>()

fun <E : Event> subscribe(eventClass: KClass<E>, action: suspend (event: E) -> Unit) {
    if (listeners[eventClass.simpleName] == null) {
        @Suppress("unchecked_cast")
        listeners[eventClass.simpleName!!] = mutableListOf(action as suspend (Event) -> Unit)
    } else {
        @Suppress("unchecked_cast")
        listeners[eventClass.simpleName]?.add(action as suspend (Event) -> Unit)
    }
}

inline fun <reified E : Event> subscribe(noinline action: suspend (event: E) -> Unit) {
    subscribe(E::class, action)
}

fun fire(event: Event) {
    listeners[event::class.simpleName]?.forEach {
        GlobalScope.launch { it(event) }
    }
}
