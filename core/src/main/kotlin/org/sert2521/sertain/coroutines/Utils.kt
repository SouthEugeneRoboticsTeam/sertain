package org.sert2521.sertain.coroutines

import kotlinx.coroutines.*
import java.lang.String.join

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
