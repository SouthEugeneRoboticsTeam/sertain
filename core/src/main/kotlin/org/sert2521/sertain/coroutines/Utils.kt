package org.sert2521.sertain.coroutines

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun periodic(period: Long, delay: Long = 0, action: () -> Unit) {
    RobotScope.launch {
        delay(delay)
        while (true) {
            action()
            delay(period)
        }
    }
}
