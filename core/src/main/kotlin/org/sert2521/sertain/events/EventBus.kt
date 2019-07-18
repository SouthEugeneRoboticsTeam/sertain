package org.sert2521.sertain.events

import kotlinx.coroutines.channels.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.sert2521.sertain.coroutines.RobotScope

val events = BroadcastChannel<Event>(Channel.BUFFERED)

fun <E : Event> fire(event: E) {
    RobotScope.launch { events.send(event) }
}

inline fun <reified T> subscribe(noinline action: suspend (T) -> Unit): Flow<T> {
    return events.asFlow().filter { it is T }.map { it as T }.apply {
        RobotScope.launch { collect(action) }
    }
}
