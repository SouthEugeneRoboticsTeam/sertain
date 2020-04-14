package org.sert2521.example.drivetrain

import org.sert2521.example.OI
import org.sert2521.example.drivetrain
import org.sert2521.sertain.coroutines.periodic
import org.sert2521.sertain.units.Chronic
import org.sert2521.sertain.units.MetricValue

suspend fun controlDrivetrain() = drivetrain {
    periodic {
        it.arcadeDrive(OI.drive, OI.turn)
    }
}

suspend fun driveForwardFor(time: MetricValue<Chronic>) = drivetrain {
    periodic(time = time) {
        it.tankDrive(0.5, 0.5)
    }
}
