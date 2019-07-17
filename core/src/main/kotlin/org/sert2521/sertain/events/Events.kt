package org.sert2521.sertain.events

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

val listeners = mutableMapOf<String, MutableList<suspend () -> Unit>>()

fun subscribe(event: String, action: suspend () -> Unit) {
    if (listeners[event] == null) {
        listeners[event] = mutableListOf(action)
    } else {
        listeners[event]?.add(action)
    }
}

fun fire(event: String) {
    listeners[event]?.forEach {
        GlobalScope.launch { it() }
    }
}
