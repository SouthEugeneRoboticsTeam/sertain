package org.sert2521.sertain

import edu.wpi.first.hal.HAL
import edu.wpi.first.wpilibj.DriverStation
import org.sert2521.sertain.core.initializeWPILib
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

fun robot(configure: Robot.() -> Unit) {
    initializeWPILib()

    // Tell the DS that the robot is ready to be enabled
    HAL.observeUserProgramStarting()

    val ds: DriverStation = DriverStation.getInstance()
    val running = true

    val robot = Robot().apply(configure)

    while (running) {
        val hasNewData = ds.waitForData(0.02)

        if (!ds.isDSAttached) {
            robot.mode = RobotMode.DISCONNECTED
        }

        if (hasNewData) {
            if (robot.mode == null || robot.mode == RobotMode.DISCONNECTED) {
                if (robot.mode != null) robot.mode = RobotMode.DISABLED
                fire(Connect)
            }

            val wasDisabled = robot.mode == RobotMode.DISABLED

            when {
                ds.isDisabled && robot.mode != RobotMode.DISABLED -> {
                    HAL.observeUserProgramDisabled()
                    robot.mode = RobotMode.DISABLED
                    fire(Disable)
                }
                ds.isAutonomous && robot.mode != RobotMode.AUTONOMOUS -> {
                    HAL.observeUserProgramAutonomous()
                    robot.mode = RobotMode.AUTONOMOUS
                    if (wasDisabled) fire(Enable)
                    fire(Auto)
                }
                ds.isOperatorControl && robot.mode != RobotMode.TELEOPERATED -> {
                    HAL.observeUserProgramTeleop()
                    robot.mode = RobotMode.TELEOPERATED
                    if (wasDisabled) fire(Enable)
                    fire(Teleop)
                }
                ds.isTest && robot.mode != RobotMode.TEST -> {
                    HAL.observeUserProgramTest()
                    robot.mode = RobotMode.TEST
                    if (wasDisabled) fire(Enable)
                    fire(Test)
                }
            }
        }
    }
}
