package org.sert2521.sertain.coroutines

import kotlinx.coroutines.*
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.coroutineContext

suspend fun periodic(period: Long, delay: Long = 0, action: () -> Unit) {
    delay(delay)
    while (true) {
        action()
        delay(period)
    }
}

suspend fun doAll(configure: ActionGroupConfigure.() -> Unit) {
    with(ActionGroupConfigure().apply(configure)) {
        val job = RobotScope.launch {
            actions.forEach { launch(block = it) }
        }
        job.join()
    }
}

suspend fun doOne(configure: ActionGroupConfigure.() -> Unit) {
    with(ActionGroupConfigure().apply(configure)) {
        val job = RobotScope.launch {
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
    internal var actions = mutableListOf<suspend CoroutineScope.() -> Unit>()

    fun action(action: suspend CoroutineScope.() -> Unit) {
        actions.add(action)
    }
}
