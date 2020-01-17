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

fun CoroutineScope.whileDisabled(action: suspend CoroutineScope.(event: Disable) -> Unit) =
        subscribeBetween<Disable, Enable>(action)

fun CoroutineScope.whileEnabled(action: suspend CoroutineScope.(event: Enable) -> Unit) =
        subscribeBetween<Enable, Disable>(action)

fun CoroutineScope.whileTeleop(action: suspend CoroutineScope.(event: Teleop) -> Unit) =
        subscribeBetween<Teleop, TeleopOver>(action)

fun CoroutineScope.whileAuto(action: suspend CoroutineScope.(event: Auto) -> Unit) =
        subscribeBetween<Auto, Disable>(action)

fun CoroutineScope.whileTest(action: suspend CoroutineScope.(event: Test) -> Unit) =
        subscribeBetween<Test, Disable>(action)
