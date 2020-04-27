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

fun CoroutineScope.whenDisabled(action: suspend CoroutineScope.(event: Disable) -> Unit) =
        subscribeBetween<Disable, Enable>(action = action)

fun CoroutineScope.whenEnabled(action: suspend CoroutineScope.(event: Enable) -> Unit) =
        subscribeBetween<Enable, Disable>(action = action)

fun CoroutineScope.whenTeleop(action: suspend CoroutineScope.(event: Teleop) -> Unit) =
        subscribeBetween<Teleop, TeleopOver>(action = action)

fun CoroutineScope.whenAuto(action: suspend CoroutineScope.(event: Auto) -> Unit) =
        subscribeBetween<Auto, Disable>(action = action)

fun CoroutineScope.whenTest(action: suspend CoroutineScope.(event: Test) -> Unit) =
        subscribeBetween<Test, Disable>(action = action)
