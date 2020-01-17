package org.sert2521.sertain.core

import edu.wpi.first.hal.FRCNetComm
import edu.wpi.first.hal.HAL
import edu.wpi.first.networktables.NetworkTableInstance
import edu.wpi.first.wpilibj.RobotBase
import edu.wpi.first.wpilibj.util.WPILibVersion
import java.io.File

const val KOTLIN_LANGUAGE = 6

fun initializeWpiLib() {
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
