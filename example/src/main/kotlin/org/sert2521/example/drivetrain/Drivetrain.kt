package org.sert2521.example.drivetrain

import kotlinx.coroutines.CoroutineScope
import org.sert2521.example.MotorIds
import org.sert2521.sertain.motors.MotorController
import org.sert2521.sertain.subsystems.Subsystem
import org.sert2521.sertain.telemetry.linkTableEntry
import org.sert2521.sertain.units.AngularUnit
import org.sert2521.sertain.units.revolutions

class Drivetrain : Subsystem() {
    private val leftDrive = MotorController(MotorIds.LEFT_FRONT, MotorIds.LEFT_REAR)
    private val rightDrive = MotorController(MotorIds.RIGHT_FRONT, MotorIds.RIGHT_REAR)

    private fun leftPosition(unit: AngularUnit) = leftDrive.position(unit)
    private fun rightPosition(unit: AngularUnit) = rightDrive.position(unit)

    fun arcadeDrive(drive: Double, turn: Double) {
        leftDrive.setPercentOutput(drive - turn)
        rightDrive.setPercentOutput(drive + turn)
    }

    fun tankDrive(left: Double, right: Double) {
        leftDrive.setPercentOutput(left)
        rightDrive.setPercentOutput(right)
    }

    override fun CoroutineScope.setup() {
        linkTableEntry("LeftPose") { leftPosition(revolutions) }
        linkTableEntry("RightPose") { rightPosition(revolutions) }
    }

    override suspend fun CoroutineScope.default() {
        controlDrivetrain()
    }
}
