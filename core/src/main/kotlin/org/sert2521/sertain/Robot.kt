package org.sert2521.sertain

import org.sert2521.sertain.events.subscribe

@DslMarker
annotation class RobotDsl

@RobotDsl
class Robot {
    var mode: RobotMode? = null

    @Suppress("unused")
    fun onConnect(action: suspend () -> Unit) {
        subscribe("connect", action)
    }

    @Suppress("unused")
    fun onDisable(action: suspend () -> Unit) {
        subscribe("disable", action)
    }

    @Suppress("unused")
    fun onEnable(action: suspend () -> Unit) {
        subscribe("enable", action)
    }

    @Suppress("unused")
    fun onTeleop(action: suspend () -> Unit) {
        subscribe("teleop", action)
    }

    @Suppress("unused")
    fun onAuto(action: suspend () -> Unit) {
        subscribe("auto", action)
    }

    @Suppress("unused")
    fun onTest(action: suspend () -> Unit) {
        subscribe("test", action)
    }
}

enum class RobotMode {
    DISCONNECTED,
    DISABLED,
    TELEOPERATED,
    AUTONOMOUS,
    TEST
}
