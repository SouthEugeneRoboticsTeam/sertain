package org.sert2521.sertain.events

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import org.sert2521.sertain.coroutines.delayUntil

@UseExperimental(ExperimentalCoroutinesApi::class)
val events = BroadcastChannel<Event>(Channel.BUFFERED)

@UseExperimental(ExperimentalCoroutinesApi::class)
suspend fun <E : Event> fire(event: E) {
    events.send(event)
}

@UseExperimental(ExperimentalCoroutinesApi::class, FlowPreview::class)
inline fun <reified E : Event> CoroutineScope.subscribe(noinline action: suspend CoroutineScope.(E) -> Unit) =
            launch {
                events.asFlow().filter { it is E }.map { it as E }.apply {
                    collect {
                        launch { action(it) }
                    }
                }
            }

@UseExperimental(ExperimentalCoroutinesApi::class, FlowPreview::class)
inline fun <T, reified E : TargetedEvent<T>> CoroutineScope.subscribe(target: T, noinline action: suspend CoroutineScope.(E) -> Unit) =
            launch {
                events.asFlow()
                        .filter { it is E && (it as? TargetedEvent<*>)?.target == target }
                        .map { it as E }
                        .apply {
                            collect {
                                launch { action(it) }
                            }
                        }
            }

@UseExperimental(ExperimentalCoroutinesApi::class, FlowPreview::class)
inline fun <reified E1 : Event, reified E2 : Event> CoroutineScope.subscribeBetween(noinline action: suspend CoroutineScope.(E1) -> Unit) =
        launch {
            events.asFlow()
                    .filter { it is E1 }
                    .map { it as E1 }
                    .apply {
                        collect {
                            launch {
                                val job = launch { action(it) }
                                delayUntil<E2>()
                                job.cancel()
                            }
                        }
                    }
        }

@UseExperimental(ExperimentalCoroutinesApi::class, FlowPreview::class)
inline fun <T, reified E1 : TargetedEvent<T>, reified E2 : TargetedEvent<T>> CoroutineScope.subscribeBetween(target: T, noinline action: suspend CoroutineScope.(E1) -> Unit) =
        launch {
            events.asFlow()
                    .filter { it is E1 && (it as? TargetedEvent<*>)?.target == target }
                    .map { it as E1 }
                    .apply {
                        collect {
                            launch {
                                val job = launch { action(it) }
                                delayUntil<T, E2>(target)
                                job.cancel()
                            }
                        }
                    }
        }
