package org.sert2521.sertain

import org.sert2521.sertain.coroutines.RobotScope
import org.sert2521.sertain.events.*

@DslMarker
annotation class RobotDsl

@RobotDsl
class Robot : RobotScope() {
    var mode: RobotMode? = null
        internal set

    @Suppress("unused")
    fun onConnect(action: suspend (event: Connect) -> Unit) {
        subscribe(action)
    }

    @Suppress("unused")
    fun onDisable(action: suspend (event: Disable) -> Unit) {
        subscribe(action)
    }

    @Suppress("unused")
    fun onEnable(action: suspend (event: Enable) -> Unit) {
        subscribe(action)
    }

    @Suppress("unused")
    fun onTeleop(action: suspend (event: Teleop) -> Unit) {
        subscribe(action)
    }

    @Suppress("unused")
    fun onAuto(action: suspend (event: Auto) -> Unit) {
        subscribe(action)
    }

    @Suppress("unused")
    fun onTest(action: suspend (event: Test) -> Unit) {
        subscribe(action)
    }
}

enum class RobotMode {
    DISCONNECTED,
    DISABLED,
    TELEOPERATED,
    AUTONOMOUS,
    TEST
}
