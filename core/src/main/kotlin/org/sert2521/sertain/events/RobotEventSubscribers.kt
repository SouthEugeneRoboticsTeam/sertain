package org.sert2521.sertain.events

import kotlinx.coroutines.CoroutineScope

fun CoroutineScope.onConnect(action: suspend CoroutineScope.(event: Connect) -> Unit) =
        subscribe(action)

fun CoroutineScope.onDisable(action: suspend CoroutineScope.(event: Disable) -> Unit) =
        subscribe(action)

fun CoroutineScope.onEnable(action: suspend CoroutineScope.(event: Enable) -> Unit) =
        subscribe(action)

fun CoroutineScope.onTeleop(action: suspend CoroutineScope.(event: Teleop) -> Unit) =
        subscribe(action)

fun CoroutineScope.onAuto(action: suspend CoroutineScope.(event: Auto) -> Unit) =
        subscribe(action)

fun CoroutineScope.onTest(action: suspend CoroutineScope.(event: Test) -> Unit) =
        subscribe(action)

fun CoroutineScope.onTick(action: suspend CoroutineScope.(event: Tick) -> Unit) =
        subscribe(action)
