package org.sert2521.sertain.pathfinding

import edu.wpi.first.wpilibj.geometry.Pose2d
import edu.wpi.first.wpilibj.geometry.Rotation2d
import edu.wpi.first.wpilibj.geometry.Translation2d
import org.sert2521.sertain.utils.Coordinates
import org.sert2521.sertain.utils.Point

data class WayPoint(val point: Point, val angle: Double) : Coordinates {
    constructor(x: Double, y: Double, angle: Double) : this(Point(x, y), angle)

    override val x get() = point.x
    override val y get() = point.y
}

fun WayPoint.toPose2d() = Pose2d(Translation2d(x, y), Rotation2d(angle))
