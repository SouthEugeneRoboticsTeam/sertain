package org.sert2521.sertain

import edu.wpi.first.hal.HAL
import edu.wpi.first.wpilibj.DriverStation
import org.sert2521.sertain.core.initializeWpiLib
import org.sert2521.sertain.coroutines.RobotScope
import org.sert2521.sertain.events.Auto
import org.sert2521.sertain.events.Connect
import org.sert2521.sertain.events.Disable
import org.sert2521.sertain.events.Enable
import org.sert2521.sertain.events.Events
import org.sert2521.sertain.events.Teleop
import org.sert2521.sertain.events.Test
import org.sert2521.sertain.events.fire
import org.sert2521.sertain.subsystems.manageTasks

object Robot {
    var mode = Mode.DISCONNECTED
        internal set

    enum class Mode {
        DISCONNECTED,
        DISABLED,
        TELEOPERATED,
        AUTONOMOUS,
        TEST
    }

    suspend fun start(configure: RobotScope.() -> Unit) {
        initializeWpiLib()

        // tell the DS that robot is ready to enable
        HAL.observeUserProgramStarting()

        val ds: DriverStation = DriverStation.getInstance()
        val running = true

        Events.manageTasks()

        RobotScope.apply(configure)

        while (running) {
            val hasNewData = ds.waitForData(0.02)

            if (!ds.isDSAttached) {
                // robot has disconnected
                mode = Mode.DISCONNECTED
            }

            if (hasNewData) {
                if (mode == Mode.DISCONNECTED) {
                    // robot has just connected to DS
                    mode = Mode.DISABLED
                    fire(Connect)
                }

                val wasDisabled = mode == Mode.DISABLED

                when {
                    ds.isDisabled && mode != Mode.DISABLED -> {
                        // robot has just been disabled
                        HAL.observeUserProgramDisabled()
                        mode = Mode.DISABLED
                        fire(Disable)
                    }
                    ds.isAutonomous && ds.isEnabled && mode != Mode.AUTONOMOUS -> {
                        // robot has just been set to autonomous
                        HAL.observeUserProgramAutonomous()
                        mode = Mode.AUTONOMOUS
                        if (wasDisabled) fire(Enable)
                        fire(Auto)
                    }
                    ds.isOperatorControl && ds.isEnabled && mode != Mode.TELEOPERATED -> {
                        // robot has just been set to teleop
                        HAL.observeUserProgramTeleop()
                        mode = Mode.TELEOPERATED
                        if (wasDisabled) fire(Enable)
                        fire(Teleop)
                    }
                    ds.isTest && ds.isEnabled && mode != Mode.TEST -> {
                        // robot has just been set to test
                        HAL.observeUserProgramTest()
                        mode = Mode.TEST
                        if (wasDisabled) fire(Enable)
                        fire(Test)
                    }
                }
            }
        }
    }
}


