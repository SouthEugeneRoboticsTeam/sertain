package org.sert2521.sertain

import edu.wpi.first.hal.FRCNetComm
import edu.wpi.first.hal.HAL
import edu.wpi.first.networktables.NetworkTableInstance
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.RobotBase
import edu.wpi.first.wpilibj.util.WPILibVersion
import org.sert2521.sertain.events.fire
import java.io.File

const val KOTLIN_LANGUAGE = 6

fun initializeWPILib() {
    // set up network tables
    val ntInstance = NetworkTableInstance.getDefault()
    ntInstance.setNetworkIdentity("Robot")
    ntInstance.startServer("/home/lvuser/networktables.ini")

    // initialize HAL
    check(HAL.initialize(500, 0)) { "Failed to initialize. Terminating." }

    // report robot language as Kotlin
    HAL.report(FRCNetComm.tResourceType.kResourceType_Language, KOTLIN_LANGUAGE)

    if (RobotBase.isReal()) {
        File("/tmp/frc_versions/FRC_Lib_Version.ini").writeText("Java ${WPILibVersion.Version}")
    }
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
                fire("connect")
            }

            val wasDisabled = robot.mode == RobotMode.DISABLED

            when {
                ds.isDisabled && robot.mode != RobotMode.DISABLED -> {
                    HAL.observeUserProgramDisabled()
                    robot.mode = RobotMode.DISABLED
                    fire("disable")
                }
                ds.isAutonomous && robot.mode != RobotMode.AUTONOMOUS -> {
                    HAL.observeUserProgramAutonomous()
                    robot.mode = RobotMode.AUTONOMOUS
                    if (wasDisabled) fire("enable")
                    fire("auto")
                }
                ds.isOperatorControl && robot.mode != RobotMode.TELEOPERATED -> {
                    HAL.observeUserProgramTeleop()
                    robot.mode = RobotMode.TELEOPERATED
                    if (wasDisabled) fire("enable")
                    fire("teleop")
                }
                ds.isTest && robot.mode != RobotMode.TEST -> {
                    HAL.observeUserProgramTest()
                    robot.mode = RobotMode.TEST
                    if (wasDisabled) fire("enable")
                    fire("test")
                }
            }
        }
    }
}
