package org.sert2521.sertain.pathfinding

import edu.wpi.first.wpilibj.controller.RamseteController
import edu.wpi.first.wpilibj.geometry.Translation2d
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator

data class PathSettings(
        val maxAcc: Double,
        val maxVel: Double,
        val trackWidth: Double,
        val getWayPoint: () -> WayPoint,
        val output: (left: Double, right: Double) -> Unit
)

suspend fun runPath(settings: PathSettings, configure: WayPointCollection.() -> Unit) {
    val wayPoints = WayPointCollection().apply(configure).wayPoints
    val kinematics = DifferentialDriveKinematics(settings.trackWidth)
    runWpilibPath(
            TrajectoryGenerator.generateTrajectory(
                    wayPoints.map { it.toPose2d() },
                    TrajectoryConfig(
                            settings.maxVel,
                            settings.maxAcc
                    ).setKinematics(kinematics)
            ),
            { settings.getWayPoint().toPose2d() },
            RamseteController(),
            kinematics,
            settings.output
    )
}

suspend fun runPath(settings: PathSettings, start: WayPoint, finish: WayPoint, configure: PointCollection.() -> Unit) {
    val points = PointCollection().apply(configure).points
    val kinematics = DifferentialDriveKinematics(settings.trackWidth)
    runWpilibPath(
            TrajectoryGenerator.generateTrajectory(
                    start.toPose2d(),
                    points.map { Translation2d(it.x, it.y) },
                    finish.toPose2d(),
                    TrajectoryConfig(
                            settings.maxVel,
                            settings.maxAcc
                    ).setKinematics(kinematics)
            ),
            { settings.getWayPoint().toPose2d() },
            RamseteController(),
            kinematics,
            settings.output
    )
}
