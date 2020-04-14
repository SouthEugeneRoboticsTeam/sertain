package org.sert2521.example

import org.sert2521.example.drivetrain.Drivetrain
import org.sert2521.example.drivetrain.driveForwardFor
import org.sert2521.sertain.events.whileAuto
import org.sert2521.sertain.robot
import org.sert2521.sertain.subsystems.add
import org.sert2521.sertain.units.s

val drivetrain = add<Drivetrain>()

suspend fun main() = robot {
    println("Hello, world!")

    whileAuto {
        driveForwardFor(5.s)
    }
}
