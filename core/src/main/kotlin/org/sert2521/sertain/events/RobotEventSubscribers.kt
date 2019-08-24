package org.sert2521.sertain.events

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun CoroutineScope.onConnect(action: suspend (event: Connect) -> Unit) {
    launch { subscribe(action) }
}

fun CoroutineScope.onDisable(action: suspend (event: Disable) -> Unit) {
    launch { subscribe(action) }
}

fun CoroutineScope.onEnable(action: suspend (event: Enable) -> Unit) {
    launch { subscribe(action) }
}

fun CoroutineScope.onTeleop(action: suspend (event: Teleop) -> Unit) {
    launch { subscribe(action) }
}

fun CoroutineScope.onAuto(action: suspend (event: Auto) -> Unit) {
    launch { subscribe(action) }
}

fun CoroutineScope.onTest(action: suspend (event: Test) -> Unit) {
    launch { subscribe(action) }
}

fun CoroutineScope.onTick(action: suspend (event: Tick) -> Unit) {
    launch { subscribe(action) }
}
