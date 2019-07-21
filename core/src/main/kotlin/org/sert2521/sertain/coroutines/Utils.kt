package org.sert2521.sertain.coroutines

import kotlinx.coroutines.delay

suspend fun periodic(period: Long, delay: Long = 0, action: () -> Unit) {
    delay(delay)
    while (true) {
        action()
        delay(period)
    }
}
