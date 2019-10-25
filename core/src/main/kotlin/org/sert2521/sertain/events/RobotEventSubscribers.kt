package org.sert2521.sertain.events

import kotlinx.coroutines.CoroutineScope

fun CoroutineScope.onConnect(action: suspend (event: Connect) -> Unit) =
        subscribe(action)

fun CoroutineScope.onDisable(action: suspend (event: Disable) -> Unit) =
        subscribe(action)

fun CoroutineScope.onEnable(action: suspend (event: Enable) -> Unit) =
        subscribe(action)

fun CoroutineScope.onTeleop(action: suspend (event: Teleop) -> Unit) =
        subscribe(action)

fun CoroutineScope.onAuto(action: suspend (event: Auto) -> Unit) =
        subscribe(action)

fun CoroutineScope.onTest(action: suspend (event: Test) -> Unit) =
        subscribe(action)

fun CoroutineScope.onTick(action: suspend (event: Tick) -> Unit) =
        subscribe(action)
