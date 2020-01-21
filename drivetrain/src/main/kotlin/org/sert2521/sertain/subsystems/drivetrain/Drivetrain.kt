package org.sert2521.sertain.subsystems.drivetrain

import edu.wpi.first.wpilibj.I2C
import org.sert2521.sertain.subsystems.Subsystem
import com.kauailabs.navx.frc.AHRS
import edu.wpi.first.wpilibj.I2C

class Drivetrain(options: Map<String, Any?>) : Subsystem(options["name"] as? String ?: "Drivetrain")  {
    val navx = (options["navxPort"] as? NavxPort)?.let {
        AHRS(SPI.Port.kMXP)
    }

    val heading get() = navx
}
