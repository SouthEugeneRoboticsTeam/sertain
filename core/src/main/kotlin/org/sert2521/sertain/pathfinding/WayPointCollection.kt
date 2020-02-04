package org.sert2521.sertain.pathfinding

import org.sert2521.sertain.units.AngularUnit
import org.sert2521.sertain.units.AngularValue
import org.sert2521.sertain.units.LinearUnit
import org.sert2521.sertain.units.LinearValue
import org.sert2521.sertain.units.Meters
import org.sert2521.sertain.units.Radians
import org.sert2521.sertain.units.convertTo
import org.sert2521.sertain.utils.Point

class WayPointCollection {
    val wayPoints = mutableListOf<WayPoint>()

    fun <L : LinearUnit, A : AngularUnit> wp(x: LinearValue<L>, y: LinearValue<L>, ang: AngularValue<A>) {
        wayPoints += WayPoint(x.convertTo(Meters).value, y.convertTo(Meters).value, ang.convertTo(Radians).value)
    }

    fun <L : LinearUnit> p(x: LinearValue<L>, y: LinearValue<L>) =
            Point(x.convertTo(Meters).value, y.convertTo(Meters).value)

    infix fun <A : AngularUnit> Point.ang(angle: AngularValue<A>) {
        wayPoints += WayPoint(this, angle.convertTo(Radians).value)
    }
}

class PointCollection {
    val points = mutableListOf<Point>()

    fun <L : LinearUnit, A : AngularUnit> p(x: LinearValue<L>, y: LinearValue<L>) {
        points += Point(x.convertTo(Meters).value, y.convertTo(Meters).value)
    }
}
