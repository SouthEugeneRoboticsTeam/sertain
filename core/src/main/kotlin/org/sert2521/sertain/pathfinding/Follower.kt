package org.sert2521.sertain.pathfinding

import edu.wpi.first.wpilibj.controller.RamseteController
import edu.wpi.first.wpilibj.geometry.Pose2d
import edu.wpi.first.wpilibj.geometry.Rotation2d
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics
import edu.wpi.first.wpilibj.trajectory.Trajectory
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator

data class PathConstraints(
        val maxVel: Double,
        val maxAcc: Double
)

class PathFollowerConfig {
    internal var getPose = { Pose2d(0.0, 0.0, Rotation2d(0.0)) }
    internal var outputVelocity = { l: Double, r: Double -> Unit }

    var constraints = DriveSettings(1.0)
        private set

    var trackWidth
        get() = constraints.trackWidth
        set(value) {
            constraints = constraints.copy(trackWidth = value)
        }

    fun pose(get: () -> Pose2d) {
        getPose = get
    }

    fun outputVelocity(set: (left: Double, right: Double) -> Unit) {
        outputVelocity = set
    }
}

data class DriveSettings(
        val trackWidth: Double
)

class PathFollower(val settings: DriveSettings, val getPose: () -> Pose2d, val outputVelocity: (left: Double, right: Double) -> Unit) {
    suspend fun follow(trajectory: Trajectory) {
        val kinematics = DifferentialDriveKinematics(settings.trackWidth)
        followWpilibPath(
                trajectory,
                getPose,
                RamseteController(),
                kinematics,
                outputVelocity
        )
    }

    suspend fun followPath(configure: WaypointPathConfig.() -> Unit) {
        val config = WaypointPathConfig().apply(configure)
        val kinematics = DifferentialDriveKinematics(settings.trackWidth)
        follow(
                TrajectoryGenerator.generateTrajectory(
                        config.waypoints,
                        TrajectoryConfig(
                                config.maxVel,
                                config.maxAcc
                        ).setKinematics(kinematics)
                )
        )
    }

    suspend fun followPath(start: Pose2d, finish: Pose2d, configure: PointPathConfig.() -> Unit) {
        val config = PointPathConfig().apply(configure)
        val kinematics = DifferentialDriveKinematics(settings.trackWidth)
        follow(
                TrajectoryGenerator.generateTrajectory(
                        start,
                        config.points,
                        finish,
                        TrajectoryConfig(
                                config.maxVel,
                                config.maxAcc
                        ).setKinematics(kinematics)
                )
        )
    }
}

fun follower(configure: PathFollowerConfig.() -> Unit) = with(PathFollowerConfig().apply(configure)) {
    PathFollower(DriveSettings(trackWidth), getPose, outputVelocity)
}
