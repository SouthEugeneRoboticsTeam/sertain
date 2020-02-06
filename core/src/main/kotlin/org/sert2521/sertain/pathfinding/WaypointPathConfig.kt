package org.sert2521.sertain.pathfinding

import edu.wpi.first.wpilibj.geometry.Pose2d
import edu.wpi.first.wpilibj.geometry.Rotation2d
import edu.wpi.first.wpilibj.geometry.Translation2d
import org.sert2521.sertain.units.AngularUnit
import org.sert2521.sertain.units.AngularValue
import org.sert2521.sertain.units.LinearUnit
import org.sert2521.sertain.units.LinearValue
import org.sert2521.sertain.units.Meters
import org.sert2521.sertain.units.Radians
import org.sert2521.sertain.units.convertTo

class WaypointPathConfig {
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

    val waypoints = mutableListOf<Pose2d>()

    fun <L : LinearUnit, A : AngularUnit> wp(x: LinearValue<L>, y: LinearValue<L>, ang: AngularValue<A>) {
        waypoints += Pose2d(x.convertTo(Meters).value, y.convertTo(Meters).value, Rotation2d(ang.convertTo(Radians).value))
    }

    fun <L : LinearUnit> p(x: LinearValue<L>, y: LinearValue<L>) =
            Translation2d(x.convertTo(Meters).value, y.convertTo(Meters).value)

    infix fun <A : AngularUnit> Translation2d.ang(angle: AngularValue<A>) {
        waypoints += Pose2d(this, Rotation2d(angle.convertTo(Radians).value))
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

    fun <L : LinearUnit> p(x: LinearValue<L>, y: LinearValue<L>) {
        points += Translation2d(x.convertTo(Meters).value, y.convertTo(Meters).value)
    }
}
