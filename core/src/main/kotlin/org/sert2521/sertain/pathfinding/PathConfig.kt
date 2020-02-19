package org.sert2521.sertain.pathfinding

import edu.wpi.first.wpilibj.geometry.Pose2d
import edu.wpi.first.wpilibj.geometry.Rotation2d
import edu.wpi.first.wpilibj.geometry.Translation2d

class WaypointPathConfig {
    var constraints = PathConstraints(1.0, 1.0)
        private set

    var maxVel
        get() = constraints.maxVel
        set(value) {
            constraints = constraints.copy(maxVel = value)
        }

    var maxAcc
        get() = constraints.maxAcc
        set(value) {
            constraints = constraints.copy(maxAcc = value)
        }

    val waypoints = mutableListOf<Pose2d>()

    fun wp(x: Double, y: Double, ang: Double) {
        waypoints += Pose2d(x, y, Rotation2d(ang))
    }

    fun p(x: Double, y: Double) =
            Translation2d(x, y)

    infix fun Translation2d.ang(angle: Double) {
        waypoints += Pose2d(this, Rotation2d(angle))
    }
}

class PointPathConfig {
    var constraints = PathConstraints(0.0, 0.0)
        private set

    var maxVel
        get() = constraints.maxVel
        set(value) {
            constraints = constraints.copy(maxVel = value)
        }

    var maxAcc
        get() = constraints.maxAcc
        set(value) {
            constraints = constraints.copy(maxAcc = value)
        }

    internal val points = mutableListOf<Translation2d>()

    fun p(x: Double, y: Double) {
        points += Translation2d(x, y)
    }
}
