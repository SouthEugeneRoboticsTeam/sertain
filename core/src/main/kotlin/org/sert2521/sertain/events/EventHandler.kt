package org.sert2521.sertain.events

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.sert2521.sertain.coroutines.RobotScope
import kotlin.reflect.KClass

private val listenerChannel = Channel<Pair<KClass<Event>, suspend (Event) -> Unit>>()
private val eventChannel = Channel<Event>()
private val listeners = mutableMapOf<String, MutableList<suspend (Event) -> Unit>>()

fun <E : Event> subscribe(eventClass: KClass<E>, action: suspend (event: E) -> Unit) {
    @Suppress("unchecked_cast")
    RobotScope.launch {
        listenerChannel.send((eventClass as KClass<Event>) to (action as suspend (Event) -> Unit))
    }
}

inline fun <reified E : Event> subscribe(noinline action: suspend (event: E) -> Unit) {
    subscribe(E::class, action)
}

fun fire(event: Event) {
    RobotScope.launch { eventChannel.send(event) }
}

fun handleEvents() {
    RobotScope.launch {
        while (true) {
            while (!eventChannel.isEmpty) {
                val event = eventChannel.receive()
                listeners[event::class.simpleName]?.iterator()?.forEach {
                    RobotScope.launch { it(event) }
                }
            }
            delay(5)
            while (!listenerChannel.isEmpty) {
                val listener = listenerChannel.receive()
                if (listeners[listener.first.simpleName] == null) {
                    listeners[listener.first.simpleName!!] = mutableListOf(listener.second)
                } else {
                    listeners[listener.first.simpleName]?.add(listener.second)
                }
            }
            delay(5)
        }
    }
}
