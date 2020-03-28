package org.sert2521.sertain.events

import kotlinx.coroutines.CoroutineScope

fun CoroutineScope.onConnect(action: suspend CoroutineScope.(event: Connect) -> Unit) =
        subscribe(action = action)

fun CoroutineScope.onDisable(action: suspend CoroutineScope.(event: Disable) -> Unit) =
        subscribe(action = action)

fun CoroutineScope.onEnable(action: suspend CoroutineScope.(event: Enable) -> Unit) =
        subscribe(action = action)

fun CoroutineScope.onTeleop(action: suspend CoroutineScope.(event: Teleop) -> Unit) =
        subscribe(action = action)

fun CoroutineScope.onAuto(action: suspend CoroutineScope.(event: Auto) -> Unit) =
        subscribe(action = action)

fun CoroutineScope.onTest(action: suspend CoroutineScope.(event: Test) -> Unit) =
        subscribe(action = action)

fun CoroutineScope.whileDisabled(action: suspend CoroutineScope.(event: Disable) -> Unit) =
        subscribeBetween<Disable, Enable>(action = action)

fun CoroutineScope.whileEnabled(action: suspend CoroutineScope.(event: Enable) -> Unit) =
        subscribeBetween<Enable, Disable>(action = action)

fun CoroutineScope.whileTeleop(action: suspend CoroutineScope.(event: Teleop) -> Unit) =
        subscribeBetween<Teleop, TeleopOver>(action = action)

fun CoroutineScope.whileAuto(action: suspend CoroutineScope.(event: Auto) -> Unit) =
        subscribeBetween<Auto, Disable>(action = action)

fun CoroutineScope.whileTest(action: suspend CoroutineScope.(event: Test) -> Unit) =
        subscribeBetween<Test, Disable>(action = action)
