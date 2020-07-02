package org.botful

import edu.wpi.first.hal.HAL
import edu.wpi.first.wpilibj.DriverStation
import kotlinx.coroutines.CoroutineScope
import org.botful.core.initializeWpiLib
import org.botful.events.Auto
import org.botful.events.AutoOver
import org.botful.events.Connect
import org.botful.events.Disable
import org.botful.events.Enable
import org.botful.events.Events
import org.botful.events.Start
import org.botful.events.Teleop
import org.botful.events.TeleopOver
import org.botful.events.Test
import org.botful.events.TestOver
import org.botful.events.fire
import org.botful.subsystems.manageTasks

object Robot {
    var mode = _root_ide_package_.org.botful.Robot.Mode.DISCONNECTED
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

    suspend fun start(configure: _root_ide_package_.org.botful.Robot.Config.() -> Unit) {
        initializeWpiLib()

        // tell the DS that robot is ready to enable
        HAL.observeUserProgramStarting()

        val ds: DriverStation = DriverStation.getInstance()
        val running = true

        manageTasks()
        _root_ide_package_.org.botful.Robot.Config().apply(configure)

        Events.fire(Start)

        while (running) {
            val hasNewData = ds.waitForData(0.02)

            if (!ds.isDSAttached) {
                // robot has disconnected
                _root_ide_package_.org.botful.Robot.mode = _root_ide_package_.org.botful.Robot.Mode.DISCONNECTED
            }

            if (hasNewData) {
                if (_root_ide_package_.org.botful.Robot.mode == _root_ide_package_.org.botful.Robot.Mode.DISCONNECTED) {
                    // robot has just connected to DS
                    _root_ide_package_.org.botful.Robot.mode = _root_ide_package_.org.botful.Robot.Mode.DISABLED
                    Events.fire(Connect)
                }

                val wasDisabled = _root_ide_package_.org.botful.Robot.mode == _root_ide_package_.org.botful.Robot.Mode.DISABLED

                when {
                    ds.isDisabled && _root_ide_package_.org.botful.Robot.mode != _root_ide_package_.org.botful.Robot.Mode.DISABLED -> {
                        // robot has just been disabled
                        HAL.observeUserProgramDisabled()
                        _root_ide_package_.org.botful.Robot.mode = _root_ide_package_.org.botful.Robot.Mode.DISABLED
                        Events.fire(Disable)
                    }
                    ds.isAutonomous && ds.isEnabled && _root_ide_package_.org.botful.Robot.mode != _root_ide_package_.org.botful.Robot.Mode.AUTONOMOUS -> {
                        // robot has just been set to autonomous
                        HAL.observeUserProgramAutonomous()
                        _root_ide_package_.org.botful.Robot.mode = _root_ide_package_.org.botful.Robot.Mode.AUTONOMOUS
                        if (wasDisabled) Events.fire(Enable)
                        Events.fire(Auto)
                    }
                    ds.isOperatorControl && ds.isEnabled && _root_ide_package_.org.botful.Robot.mode != _root_ide_package_.org.botful.Robot.Mode.TELEOPERATED -> {
                        // robot has just been set to teleop
                        HAL.observeUserProgramTeleop()
                        _root_ide_package_.org.botful.Robot.mode = _root_ide_package_.org.botful.Robot.Mode.TELEOPERATED
                        if (wasDisabled) Events.fire(Enable)
                        Events.fire(Teleop)
                    }
                    ds.isTest && ds.isEnabled && _root_ide_package_.org.botful.Robot.mode != _root_ide_package_.org.botful.Robot.Mode.TEST -> {
                        // robot has just been set to test
                        HAL.observeUserProgramTest()
                        _root_ide_package_.org.botful.Robot.mode = _root_ide_package_.org.botful.Robot.Mode.TEST
                        if (wasDisabled) Events.fire(Enable)
                        Events.fire(Test)
                    }
                }
            }
        }
    }
}
