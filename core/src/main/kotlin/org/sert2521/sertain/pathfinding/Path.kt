package org.sert2521.sertain.pathfinding

import edu.wpi.first.wpilibj.geometry.Pose2d
import edu.wpi.first.wpilibj.geometry.Rotation2d
import edu.wpi.first.wpilibj.geometry.Translation2d
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics
import edu.wpi.first.wpilibj.trajectory.Trajectory
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator

data class RobotConfig(
        val trackWidth: Double
)

data class PathConstraints(
        val maxVel: Double,
        val maxAcc: Double
)

class Path private constructor(val settings: RobotConfig, val constraints: PathConstraints, private val wpilibTrajectory: Trajectory) {
    constructor(settings: RobotConfig, constraints: PathConstraints, vararg waypoints: Pose2d) : this(
            settings,
            constraints,
            TrajectoryGenerator.generateTrajectory(
                    waypoints.map { it },
                    TrajectoryConfig(
                            constraints.maxVel,
                            constraints.maxAcc
                    ).setKinematics(DifferentialDriveKinematics(settings.trackWidth))
            )
    )

    constructor(settings: RobotConfig, constraints: PathConstraints, start: Pose2d, finish: Pose2d, vararg points: Translation2d) : this(
            settings,
            constraints,
            TrajectoryGenerator.generateTrajectory(
                    start,
                    points.toList(),
                    finish,
                    TrajectoryConfig(
                            constraints.maxVel,
                            constraints.maxAcc
                    ).setKinematics(DifferentialDriveKinematics(settings.trackWidth))
            )
    )

    val startPoint = wpilibTrajectory.initialPose.run { Pose2d(translation.x, translation.y, Rotation2d(rotation.radians)) }
    val length get() = wpilibTrajectory.totalTimeSeconds

    operator fun get(t: Double) = wpilibTrajectory.sample(t)

    fun relativeTo(point: Pose2d) = Path(settings, constraints, wpilibTrajectory.relativeTo(point))

    fun toWpilibTrajectory() = wpilibTrajectory
}
