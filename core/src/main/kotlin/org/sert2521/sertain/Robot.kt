package org.sert2521.sertain

import edu.wpi.first.hal.HAL
import edu.wpi.first.wpilibj.DriverStation
import kotlinx.coroutines.launch
import org.sert2521.sertain.core.initializeWpiLib
import org.sert2521.sertain.coroutines.RobotScope
import org.sert2521.sertain.events.Auto
import org.sert2521.sertain.events.Connect
import org.sert2521.sertain.events.Disable
import org.sert2521.sertain.events.Enable
import org.sert2521.sertain.events.Teleop
import org.sert2521.sertain.events.Test
import org.sert2521.sertain.events.fire
import org.sert2521.sertain.subsystems.Workers
import org.sert2521.sertain.subsystems.manageTasks

object Robot {
    var mode = RobotMode.DISCONNECTED
        internal set
}

enum class RobotMode {
    DISCONNECTED,
    DISABLED,
    TELEOPERATED,
    AUTONOMOUS,
    TEST
}

suspend fun robot(configure: RobotScope.() -> Unit) {
    initializeWpiLib()

    // tell the DS that robot is ready to enable
    HAL.observeUserProgramStarting()

    val ds: DriverStation = DriverStation.getInstance()
    val running = true

    Workers.setupAll()

    RobotScope.launch {
        manageTasks()
    }

    Workers.defaultAll()

    RobotScope.apply(configure)

    while (running) {
        val hasNewData = ds.waitForData(0.02)

        if (!ds.isDSAttached) {
            // robot has disconnected
            Robot.mode = RobotMode.DISCONNECTED
        }

        if (hasNewData) {
            if (Robot.mode == RobotMode.DISCONNECTED) {
                // robot has just connected to DS
                Robot.mode = RobotMode.DISABLED
                RobotScope.fire(Connect)
            }

            val wasDisabled = Robot.mode == RobotMode.DISABLED

            when {
                ds.isDisabled && Robot.mode != RobotMode.DISABLED -> {
                    // robot has just been disabled
                    HAL.observeUserProgramDisabled()
                    Robot.mode = RobotMode.DISABLED
                    RobotScope.fire(Disable)
                }
                ds.isAutonomous && ds.isEnabled && Robot.mode != RobotMode.AUTONOMOUS -> {
                    // robot has just been set to autonomous
                    HAL.observeUserProgramAutonomous()
                    Robot.mode = RobotMode.AUTONOMOUS
                    if (wasDisabled) RobotScope.fire(Enable)
                    RobotScope.fire(Auto)
                }
                ds.isOperatorControl && ds.isEnabled && Robot.mode != RobotMode.TELEOPERATED -> {
                    // robot has just been set to teleop
                    HAL.observeUserProgramTeleop()
                    Robot.mode = RobotMode.TELEOPERATED
                    if (wasDisabled) RobotScope.fire(Enable)
                    RobotScope.fire(Teleop)
                }
                ds.isTest && ds.isEnabled && Robot.mode != RobotMode.TEST -> {
                    // robot has just been set to test
                    HAL.observeUserProgramTest()
                    Robot.mode = RobotMode.TEST
                    if (wasDisabled) RobotScope.fire(Enable)
                    RobotScope.fire(Test)
                }
            }
        }
    }
}
