package org.sert2521.sertain.events

import kotlinx.coroutines.channels.*
import kotlinx.coroutines.flow.*

val events = BroadcastChannel<Event>(Channel.BUFFERED)

suspend fun <E : Event> fire(event: E) {
    events.send(event)
}

suspend inline fun <reified E> subscribe(noinline action: suspend (E) -> Unit) =
        events.asFlow().filter { it is E }.map { it as E }.apply { collect(action) }

suspend inline fun <T, reified E : TargetedEvent<T>> subscribe(target: T, noinline action: suspend (E) -> Unit): Flow<E> =
        events.asFlow()
            .filter { it is E && (it as? TargetedEvent<T>)?.target == target }
            .map { it as E }
            .apply { collect(action) }
