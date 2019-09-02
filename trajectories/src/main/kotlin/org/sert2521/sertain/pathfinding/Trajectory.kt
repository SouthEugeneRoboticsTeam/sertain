package org.sert2521.sertain.pathfinding

import java.nio.file.Path

class Trajectory(val segments: List<Segment>) {

}

data class Segment(
        val pos: Double,
        val vel: Double,
        val acc: Double,
        val jerk: Double,
        val ang: Double,
        val dt: Double,
        val x: Double,
        val y: Double
)

fun Path2D.toTrajectory(configure: Path2DConfigure) {
    if (waypoints.size < 2) {
        error("Replace this error")
        // TODO some error handling
    }

    val splines = mutableListOf<Spline>()
    val splineLengths = mutableListOf<Double>()

}

// Split a trajectory into right and left trajectories
fun split(trajectory: Trajectory, wheelbaseWidth: Double): Pair<Trajectory, Trajectory> {

}
