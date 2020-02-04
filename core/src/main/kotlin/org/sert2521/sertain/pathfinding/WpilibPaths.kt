package org.sert2521.sertain.pathfinding

import edu.wpi.first.wpilibj.controller.RamseteController
import edu.wpi.first.wpilibj.geometry.Pose2d
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics
import edu.wpi.first.wpilibj.trajectory.Trajectory
import org.sert2521.sertain.utils.timer

suspend fun runWpilibPath(
        trajectory: Trajectory,
        getPose: () -> Pose2d,
        follower: RamseteController,
        kinematics: DifferentialDriveKinematics,
        outputMetersPerSecond: (left: Double, right: Double) -> Unit
) {
    timer(20, 0, (trajectory.totalTimeSeconds * 1000).toLong().also { println("Path time: $it") }) {
        val curTime = it.toDouble() / 1000

        val targetWheelSpeeds = kinematics.toWheelSpeeds(
                follower.calculate(getPose(), trajectory.sample(curTime))
        )

        val leftSpeedSetpoint = targetWheelSpeeds.leftMetersPerSecond
        val rightSpeedSetpoint = targetWheelSpeeds.rightMetersPerSecond

        outputMetersPerSecond(leftSpeedSetpoint, rightSpeedSetpoint)
    }
}
