package org.sert2521.sertain.coroutines

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import org.sert2521.sertain.events.Tick
import org.sert2521.sertain.events.subscribe

suspend fun periodic(period: Long, delay: Long = 0, action: () -> Unit) = coroutineScope {
    delay(delay)
    while (true) {
        action()
        delay(period)
    }
}

fun onTick(action: suspend (event: Tick) -> Unit) {
    subscribe(action)
}
