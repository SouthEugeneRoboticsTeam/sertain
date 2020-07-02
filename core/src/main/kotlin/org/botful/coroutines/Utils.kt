package org.botful.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.botful.events.Event
import org.botful.events.Events
import org.botful.units.Chronic
import org.botful.units.MetricUnit
import org.botful.units.MetricValue
import org.botful.units.convert
import org.botful.units.from
import org.botful.units.milliseconds
import org.botful.units.ms
import kotlin.coroutines.coroutineContext

suspend fun delay(t: MetricValue<Chronic>) = delay(t.from(milliseconds).toLong())

suspend fun delayUntil(condition: () -> Boolean) {
    while (!condition()) {
        delay(20)
    }
}

suspend inline fun <reified E : Event> delayUntil() {
    val job = CoroutineScope(coroutineContext).launch {
        delayForever()
    }
    val sub = Events.subscribe<E> {
        job.cancel()
    }
    job.join()
    Events.remove(sub)
}

suspend inline fun <reified E : Event.Targeted<*>> delayUntil(target: Any?) {
    val job = CoroutineScope(coroutineContext).launch {
        delayForever()
    }
    val s = Events.subscribe<E>(target) {
        job.cancel()
    }
    job.join()
    Events.remove(s)
}

suspend fun delayForever() {
    while (true) {
        delay(2000)
    }
}

suspend inline fun periodic(
        period: MetricValue<Chronic> = 20.ms,
        delay: MetricValue<Chronic> = 0.ms,
        time: MetricValue<Chronic> = 0.ms,
        crossinline action: () -> Unit
) {
    val job = CoroutineScope(coroutineContext).launch {
        delay(delay)
        while (true) {
            action()
            delay(period)
        }
    }
    if (time.value > 0) {
        delay(time)
        job.cancelAndJoin()
    }
}

suspend inline fun timer(
        period: MetricValue<Chronic> = 20.ms,
        delay: MetricValue<Chronic> = 0.ms,
        time: MetricValue<Chronic> = 0.ms,
        crossinline action: (MetricValue<Chronic>) -> Unit
) {
    val startTime = System.currentTimeMillis()
    periodic(period, delay, time) {
        action((System.currentTimeMillis() - startTime).ms)
    }
}

suspend inline fun timer(
        period: MetricValue<Chronic> = 20.ms,
        delay: MetricValue<Chronic> = 0.ms,
        time: MetricValue<Chronic> = 0.ms,
        unit: MetricUnit<Chronic>,
        crossinline action: (Double) -> Unit
) {
    val startTime = System.currentTimeMillis()
    periodic(period, delay, time) {
        action((System.currentTimeMillis() - startTime).toDouble().convert(period.unit to unit))
    }
}

suspend fun CoroutineScope.doAll(configure: ActionGroupConfigure.() -> Unit) {
    with(ActionGroupConfigure().apply(configure)) {
        val job = launch {
            actions.forEach {
                launch(block = it)
            }
        }
        job.join()
    }
}

suspend fun CoroutineScope.doOne(configure: ActionGroupConfigure.() -> Unit) {
    with(ActionGroupConfigure().apply(configure)) {
        val job = launch {
            val scope = this
            actions.forEach {
                launch {
                    try {
                        it()
                    } finally {
                        scope.cancel()
                    }
                }
            }
        }
        job.join()
    }
}

class ActionGroupConfigure {
    internal val actions = mutableListOf<suspend CoroutineScope.() -> Unit>()

    fun action(action: suspend CoroutineScope.() -> Unit) {
        actions.add(action)
    }
}
