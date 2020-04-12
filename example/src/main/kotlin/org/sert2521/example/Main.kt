package org.sert2521.example

import org.sert2521.example.drivetrain.Drivetrain
import org.sert2521.sertain.robot
import org.sert2521.sertain.subsystems.add

val drivetrain = add<Drivetrain>()

suspend fun main() = robot {
    println("Hello, world!")
}
