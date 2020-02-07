package org.sert2521.sertain.pathfinding

import edu.wpi.first.wpilibj.controller.RamseteController
import edu.wpi.first.wpilibj.geometry.Pose2d
import edu.wpi.first.wpilibj.geometry.Rotation2d
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator

class PathFollowerConfig {
    internal var getPose = { Pose2d(0.0, 0.0, Rotation2d(0.0)) }
    internal var outputVelocity = { l: Double, r: Double -> Unit }

    var constraints = DriveSettings(0.0)
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
    suspend fun follow(path: Path) {
        runWpilibPath(
                path.toWpilibTrajectory(),
                getPose,
                RamseteController(),
                DifferentialDriveKinematics(path.settings.trackWidth),
                outputVelocity
        )
    }

    suspend fun followPath(configure: WaypointPathConfig.() -> Unit) {
        val config = WaypointPathConfig().apply(configure)
        val kinematics = DifferentialDriveKinematics(settings.trackWidth)
        runWpilibPath(
                TrajectoryGenerator.generateTrajectory(
                        config.waypoints,
                        TrajectoryConfig(
                                config.maxVel,
                                config.maxAcc
                        ).setKinematics(kinematics)
                ),
                getPose,
                RamseteController(),
                kinematics,
                outputVelocity
        )
    }

    suspend fun followPath(start: Pose2d, finish: Pose2d, configure: PointPathConfig.() -> Unit) {
        val config = PointPathConfig().apply(configure)
        val kinematics = DifferentialDriveKinematics(settings.trackWidth)
        runWpilibPath(
                TrajectoryGenerator.generateTrajectory(
                        start,
                        config.points,
                        finish,
                        TrajectoryConfig(
                                config.maxVel,
                                config.maxAcc
                        ).setKinematics(kinematics)
                ),
                getPose,
                RamseteController(),
                kinematics,
                outputVelocity
        )
    }
}

fun follower(configure: PathFollowerConfig.() -> Unit) = with(PathFollowerConfig().apply(configure)) {
    PathFollower(DriveSettings(trackWidth), getPose, outputVelocity)
}
