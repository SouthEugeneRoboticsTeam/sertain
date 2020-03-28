package org.sert2521.sertain.events

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import org.sert2521.sertain.coroutines.delayUntil
import kotlin.coroutines.EmptyCoroutineContext

@UseExperimental(ExperimentalCoroutinesApi::class)
val events = BroadcastChannel<Event>(Channel.BUFFERED)

@UseExperimental(ExperimentalCoroutinesApi::class)
fun <E : Event> CoroutineScope.fire(event: E) = launch {
    events.send(event)
}

@UseExperimental(ExperimentalCoroutinesApi::class, FlowPreview::class)
inline fun <reified E : Event> CoroutineScope.subscribe(context: CoroutineContext = EmptyCoroutineContext, noinline action: suspend CoroutineScope.(E) -> Unit) =
        launch(context) {
            events.asFlow().filter { it is E }.map { it as E }.collect {
                launch(context) { action(it) }
            }
        }

@UseExperimental(ExperimentalCoroutinesApi::class, FlowPreview::class)
inline fun <T, reified E : TargetedEvent<T>> CoroutineScope.subscribe(target: T, context: CoroutineContext = EmptyCoroutineContext, noinline action: suspend CoroutineScope.(E) -> Unit) =
        launch(context) {
            events.asFlow()
                    .filter { it is E && (it as? TargetedEvent<*>)?.target == target }
                    .map { it as E }
                    .collect {
                        launch(context) { action(it) }
                    }
        }

@UseExperimental(ExperimentalCoroutinesApi::class, FlowPreview::class)
inline fun <reified E1 : Event, reified E2 : Event> CoroutineScope.subscribeBetween(context: CoroutineContext = EmptyCoroutineContext, noinline action: suspend CoroutineScope.(E1) -> Unit) =
        launch(context) {
            events.asFlow()
                    .filter { it is E1 }
                    .map { it as E1 }
                    .apply {
                        collect {
                            launch(context) {
                                val job = launch(context) { action(it) }
                                delayUntil<E2>()
                                job.cancel()
                            }
                        }
                    }
        }

@UseExperimental(ExperimentalCoroutinesApi::class, FlowPreview::class)
inline fun <T, reified E1 : TargetedEvent<T>, reified E2 : TargetedEvent<T>> CoroutineScope.subscribeBetween(target: T, context: CoroutineContext = EmptyCoroutineContext, noinline action: suspend CoroutineScope.(E1) -> Unit) =
        launch(context) {
            events.asFlow()
                    .filter { it is E1 && (it as? TargetedEvent<*>)?.target == target }
                    .map { it as E1 }
                    .collect {
                        launch(context) {
                            val job = launch(context) { action(it) }
                            delayUntil<T, E2>(target)
                            job.cancel()
                        }
                    }
        }
