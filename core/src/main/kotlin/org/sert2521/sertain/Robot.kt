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

    // tell the DS that robot is ready to enable
    HAL.observeUserProgramStarting()

    val ds: DriverStation = DriverStation.getInstance()
    val running = true

    val robot = Robot().apply(configure)

    while (running) {
        val hasNewData = ds.waitForData(0.02)

        if (!ds.isDSAttached) {
            // robot has disconnected
            robot.mode = RobotMode.DISCONNECTED
        }

        if (hasNewData) {
            if (robot.mode == null || robot.mode == RobotMode.DISCONNECTED) {
                // robot has just connected to DS
                if (robot.mode != null) robot.mode = RobotMode.DISABLED
                fire(Connect)
            }

            val wasDisabled = robot.mode == RobotMode.DISABLED

            when {
                ds.isDisabled && robot.mode != RobotMode.DISABLED -> {
                    // robot has just been disabled
                    HAL.observeUserProgramDisabled()
                    robot.mode = RobotMode.DISABLED
                    fire(Disable)
                }
                ds.isAutonomous && robot.mode != RobotMode.AUTONOMOUS -> {
                    // robot has just been set to autonomous
                    HAL.observeUserProgramAutonomous()
                    robot.mode = RobotMode.AUTONOMOUS
                    if (wasDisabled) fire(Enable)
                    fire(Auto)
                }
                ds.isOperatorControl && robot.mode != RobotMode.TELEOPERATED -> {
                    // robot has just been set to teleop
                    HAL.observeUserProgramTeleop()
                    robot.mode = RobotMode.TELEOPERATED
                    if (wasDisabled) fire(Enable)
                    fire(Teleop)
                }
                ds.isTest && robot.mode != RobotMode.TEST -> {
                    // robot has just been set to test
                    HAL.observeUserProgramTest()
                    robot.mode = RobotMode.TEST
                    if (wasDisabled) fire(Enable)
                    fire(Test)
                }
            }
        }
    }
}
