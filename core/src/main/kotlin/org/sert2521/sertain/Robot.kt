package org.sert2521.sertain

import edu.wpi.first.hal.HAL
import edu.wpi.first.wpilibj.DriverStation
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
import org.sert2521.sertain.subsystems.Subsystem
import org.sert2521.sertain.subsystems.TaskConfigure
import org.sert2521.sertain.subsystems.manageSubsystems
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class Robot {
    var mode = RobotMode.DISCONNECTED
        internal set

    internal var subsystems = mutableMapOf<KClass<*>, Subsystem>()

    fun <S : Subsystem> add(reference: KClass<*>, subsystem: S) {
        subsystems[reference] = subsystem
    }

    fun <S : Subsystem> add(subsystem: S) {
        add(subsystem::class, subsystem)
    }

    inline fun <reified S : Subsystem> add() {
        add(S::class, S::class.createInstance())
    }

    fun <S : Subsystem> access(reference: KClass<S>): S {
        @Suppress("unchecked_cast") // Safe because subsystems is internally managed
        subsystems[reference]?.let {
            return it as S
        }
        throw IllegalStateException("Subsystem with type ${reference.qualifiedName} does not exist." +
                " Did you forget to add it?")
    }

    inline fun <reified S : Subsystem> access() = access(S::class)

    inline fun <reified S : Subsystem> TaskConfigure.use(): S {
        val subsystem = access(S::class)
        this += subsystem
        return subsystem
    }
}

enum class RobotMode {
    DISCONNECTED,
    DISABLED,
    TELEOPERATED,
    AUTONOMOUS,
    TEST
}

fun robot(configure: Robot.() -> Unit) = runBlocking {
    initializeWpiLib()

    // tell the DS that robot is ready to enable
    HAL.observeUserProgramStarting()

    val ds: DriverStation = DriverStation.getInstance()
    val running = true

    manageSubsystems()

    val robot = Robot().apply(configure)

    RobotScope.launch {
        periodic(20) {
            launch {
                fire(Tick)
            }
        }
    }

    while (running) {
        val hasNewData = ds.waitForData(0.02)

        if (!ds.isDSAttached) {
            // robot has disconnected
            robot.mode = RobotMode.DISCONNECTED
        }

        if (hasNewData) {
            if (robot.mode == RobotMode.DISCONNECTED) {
                // robot has just connected to DS
                robot.mode = RobotMode.DISABLED
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
                ds.isAutonomous && ds.isEnabled && robot.mode != RobotMode.AUTONOMOUS -> {
                    // robot has just been set to autonomous
                    HAL.observeUserProgramAutonomous()
                    robot.mode = RobotMode.AUTONOMOUS
                    if (wasDisabled) fire(Enable)
                    fire(Auto)
                }
                ds.isOperatorControl && ds.isEnabled && robot.mode != RobotMode.TELEOPERATED -> {
                    // robot has just been set to teleop
                    HAL.observeUserProgramTeleop()
                    robot.mode = RobotMode.TELEOPERATED
                    if (wasDisabled) fire(Enable)
                    fire(Teleop)
                }
                ds.isTest && ds.isEnabled && robot.mode != RobotMode.TEST -> {
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
