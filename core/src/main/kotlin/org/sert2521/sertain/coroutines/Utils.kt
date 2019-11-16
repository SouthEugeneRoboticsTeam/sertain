package org.sert2521.sertain.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.sert2521.sertain.Robot
import org.sert2521.sertain.events.Change
import org.sert2521.sertain.events.fire
import org.sert2521.sertain.events.onTick
import org.sert2521.sertain.events.subscribe

suspend fun periodic(period: Long, delay: Long = 0, action: () -> Unit) {
    delay(delay)
    while (true) {
        action()
        delay(period)
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

suspend fun delayUntil(condition: () -> Boolean) {
    while (!condition()) {
        delay(20)
    }
}

suspend fun delayForever() {
    while (true) {
        delay(2000)
    }
}

open class Observable<T>(val get: () -> T) {
    val value get() = get()
    
    init {
        var lastValue = value
        Robot.onTick {
            if (lastValue != value) fire(Change(this, value))
            lastValue = value
        }
    }

    operator fun invoke(configure: Observable<T>.() -> Unit) = apply(configure)

    fun CoroutineScope.onChange(action: suspend (event: Change<T>) -> Unit) =
            subscribe(this@Observable, action)
}

fun <T> (() -> T).watch(configure: Observable<T>.() -> Unit = {}) = Observable(this).apply(configure)
