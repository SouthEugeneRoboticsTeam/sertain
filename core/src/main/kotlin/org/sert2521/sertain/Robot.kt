package org.sert2521.sertain

import edu.wpi.first.hal.HAL
import edu.wpi.first.wpilibj.DriverStation
import kotlinx.coroutines.launch
import org.sert2521.sertain.core.initializeWpiLib
import org.sert2521.sertain.coroutines.RobotScope
import org.sert2521.sertain.coroutines.periodic
import org.sert2521.sertain.events.Auto
import org.sert2521.sertain.events.Connect
import org.sert2521.sertain.events.Disable
import org.sert2521.sertain.events.Enable
import org.sert2521.sertain.events.Teleop
import org.sert2521.sertain.events.Test
import org.sert2521.sertain.events.Tick
import org.sert2521.sertain.events.fire
import org.sert2521.sertain.subsystems.manageTasks
import org.sert2521.sertain.subsystems.subsystems
import org.sert2521.sertain.subsystems.use
import org.sert2521.sertain.units.MetricValue
import org.sert2521.sertain.units.convertTo
import org.sert2521.sertain.units.degrees
import org.sert2521.sertain.units.div
import org.sert2521.sertain.units.meters
import org.sert2521.sertain.units.millimeters
import org.sert2521.sertain.units.mmps
import org.sert2521.sertain.units.mps
import org.sert2521.sertain.units.mpss
import org.sert2521.sertain.units.rad
import org.sert2521.sertain.units.radians
import org.sert2521.sertain.units.rdps
import org.sert2521.sertain.units.revolutions
import org.sert2521.sertain.units.rpss
import org.sert2521.sertain.units.seconds
import org.sert2521.sertain.units.times
import kotlin.math.PI

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

    RobotScope.apply(configure)

    RobotScope.launch {
        manageTasks()
        periodic(20) {
            launch {
                fire(Tick)
            }
        }
    }

    subsystems
            .forEach {
                it.value.default?.apply {
                    RobotScope.launch {
                        use(it.value, name = "Default") {
                            invoke()
                        }
                    }
                }
            }

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
                fire(Connect)
            }

            val wasDisabled = Robot.mode == RobotMode.DISABLED

            when {
                ds.isDisabled && Robot.mode != RobotMode.DISABLED -> {
                    // robot has just been disabled
                    HAL.observeUserProgramDisabled()
                    Robot.mode = RobotMode.DISABLED
                    fire(Disable)
                }
                ds.isAutonomous && ds.isEnabled && Robot.mode != RobotMode.AUTONOMOUS -> {
                    // robot has just been set to autonomous
                    HAL.observeUserProgramAutonomous()
                    Robot.mode = RobotMode.AUTONOMOUS
                    if (wasDisabled) fire(Enable)
                    fire(Auto)
                }
                ds.isOperatorControl && ds.isEnabled && Robot.mode != RobotMode.TELEOPERATED -> {
                    // robot has just been set to teleop
                    HAL.observeUserProgramTeleop()
                    Robot.mode = RobotMode.TELEOPERATED
                    if (wasDisabled) fire(Enable)
                    fire(Teleop)
                }
                ds.isTest && ds.isEnabled && Robot.mode != RobotMode.TEST -> {
                    // robot has just been set to test
                    HAL.observeUserProgramTest()
                    Robot.mode = RobotMode.TEST
                    if (wasDisabled) fire(Enable)
                    fire(Test)
                }
            }
        }
    }
}
