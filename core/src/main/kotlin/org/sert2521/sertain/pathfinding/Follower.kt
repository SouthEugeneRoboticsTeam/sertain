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

    fun pose(get: () -> Pose2d) {
        getPose = get
    }

    fun outputVelocity(set: (left: Double, right: Double) -> Unit) {
        outputVelocity = set
    }
}

data class PathFollower(val getPose: () -> Pose2d, val outputVelocity: (left: Double, right: Double) -> Unit) {
    suspend fun follow(path: Path) {
        runWpilibPath(
                path.toWpilibTrajectory(),
                getPose,
                RamseteController(),
                DifferentialDriveKinematics(path.settings.trackWidth),
                outputVelocity
        )
    }

    suspend fun followPath(settings: RobotConfig, constraints: PathConstraints, configure: WaypointPathConfig.() -> Unit) {
        val waypoints = WaypointPathConfig().apply(configure).waypoints
        val kinematics = DifferentialDriveKinematics(settings.trackWidth)
        runWpilibPath(
                TrajectoryGenerator.generateTrajectory(
                        waypoints,
                        TrajectoryConfig(
                                constraints.maxVel,
                                constraints.maxAcc
                        ).setKinematics(kinematics)
                ),
                getPose,
                RamseteController(),
                kinematics,
                outputVelocity
        )
    }

    suspend fun followPath(settings: RobotConfig, constraints: PathConstraints, start: Pose2d, finish: Pose2d, configure: PointPathConfig.() -> Unit) {
        val points = PointPathConfig().apply(configure).points
        val kinematics = DifferentialDriveKinematics(settings.trackWidth)
        runWpilibPath(
                TrajectoryGenerator.generateTrajectory(
                        start,
                        points,
                        finish,
                        TrajectoryConfig(
                                constraints.maxVel,
                                constraints.maxAcc
                        ).setKinematics(kinematics)
                ),
                getPose,
                RamseteController(),
                kinematics,
                outputVelocity
        )
    }
}

fun pathFollower(configure: PathFollowerConfig.() -> Unit) = with(PathFollowerConfig().apply(configure)) {
    PathFollower(getPose, outputVelocity)
}
