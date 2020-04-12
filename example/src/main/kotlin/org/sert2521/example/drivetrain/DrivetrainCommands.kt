package org.sert2521.example.drivetrain

import org.sert2521.example.Controls
import org.sert2521.example.drivetrain
import org.sert2521.sertain.coroutines.periodic
import javax.naming.ldap.Control

suspend fun controlDrivetrain() = drivetrain {
    periodic {
        it.arcadeDrive(Controls, getTurn())
    }
}
