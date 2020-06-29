package org.sert2521.sertain

import edu.wpi.first.hal.HAL
import edu.wpi.first.wpilibj.DriverStation
import kotlinx.coroutines.CoroutineScope
import org.sert2521.sertain.core.initializeWpiLib
import org.sert2521.sertain.events.Auto
import org.sert2521.sertain.events.AutoOver
import org.sert2521.sertain.events.Connect
import org.sert2521.sertain.events.Disable
import org.sert2521.sertain.events.Enable
import org.sert2521.sertain.events.Events
import org.sert2521.sertain.events.Start
import org.sert2521.sertain.events.Teleop
import org.sert2521.sertain.events.TeleopOver
import org.sert2521.sertain.events.Test
import org.sert2521.sertain.events.TestOver
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

    class Config internal constructor() {
        fun onStart(action: suspend CoroutineScope.(Start) -> Unit) = Events.subscribe(action)

        fun onEnable(action: suspend CoroutineScope.(Enable) -> Unit) = Events.subscribe(action)
        fun onDisable(action: suspend CoroutineScope.(Disable) -> Unit) = Events.subscribe(action)
        fun onTeleop(action: suspend CoroutineScope.(Teleop) -> Unit) = Events.subscribe(action)
        fun onAuto(action: suspend CoroutineScope.(Auto) -> Unit) = Events.subscribe(action)
        fun onTest(action: suspend CoroutineScope.(Test) -> Unit) = Events.subscribe(action)

        fun whenEnable(action: suspend CoroutineScope.(Enable) -> Unit) = Events.between<Enable, Disable>(action)
        fun whenDisable(action: suspend CoroutineScope.(Disable) -> Unit) = Events.between<Disable, Enable>(action)
        fun whenTeleop(action: suspend CoroutineScope.(Teleop) -> Unit) = Events.between<Teleop, TeleopOver>(action)
        fun whenAuto(action: suspend CoroutineScope.(Auto) -> Unit) = Events.between<Auto, AutoOver>(action)
        fun whenTest(action: suspend CoroutineScope.(Test) -> Unit) = Events.between<Test, TestOver>(action)
    }

    suspend fun start(configure: Config.() -> Unit) {
        initializeWpiLib()

        // tell the DS that robot is ready to enable
        HAL.observeUserProgramStarting()

        val ds: DriverStation = DriverStation.getInstance()
        val running = true

        manageTasks()
        Config().apply(configure)

        Events.fire(Start)

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
                    Events.fire(Connect)
                }

                val wasDisabled = mode == Mode.DISABLED

                when {
                    ds.isDisabled && mode != Mode.DISABLED -> {
                        // robot has just been disabled
                        HAL.observeUserProgramDisabled()
                        mode = Mode.DISABLED
                        Events.fire(Disable)
                    }
                    ds.isAutonomous && ds.isEnabled && mode != Mode.AUTONOMOUS -> {
                        // robot has just been set to autonomous
                        HAL.observeUserProgramAutonomous()
                        mode = Mode.AUTONOMOUS
                        if (wasDisabled) Events.fire(Enable)
                        Events.fire(Auto)
                    }
                    ds.isOperatorControl && ds.isEnabled && mode != Mode.TELEOPERATED -> {
                        // robot has just been set to teleop
                        HAL.observeUserProgramTeleop()
                        mode = Mode.TELEOPERATED
                        if (wasDisabled) Events.fire(Enable)
                        Events.fire(Teleop)
                    }
                    ds.isTest && ds.isEnabled && mode != Mode.TEST -> {
                        // robot has just been set to test
                        HAL.observeUserProgramTest()
                        mode = Mode.TEST
                        if (wasDisabled) Events.fire(Enable)
                        Events.fire(Test)
                    }
                }
            }
        }
    }
}
