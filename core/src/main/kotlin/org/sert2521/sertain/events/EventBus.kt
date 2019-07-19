package org.sert2521.sertain.events

import kotlinx.coroutines.channels.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.sert2521.sertain.coroutines.RobotScope

val events = BroadcastChannel<Event>(Channel.BUFFERED)

fun <E : Event> fire(event: E) {
    RobotScope.launch { events.send(event) }
}

inline fun <reified E> subscribe(noinline action: suspend (E) -> Unit): Flow<E> {
    return events.asFlow().filter { it is E }.map { it as E }.apply {
        RobotScope.launch { collect(action) }
    }
}

inline fun <T, reified E : Event> T.subscribe(noinline action: suspend (E) -> Unit): Flow<E> {
    return events.asFlow()
            .filter { it is E && ((it as? TargetedEvent<T>) == null || (it as? TargetedEvent<T>)?.target == this) }
            .map { it as E }
            .apply { RobotScope.launch { collect(action) } }
}
