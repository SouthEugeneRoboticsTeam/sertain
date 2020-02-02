package org.sert2521.sertain.pathfinding

import org.sert2521.sertain.utils.Point
import org.sert2521.sertain.utils.p

data class Waypoint(
        override val x: Double,
        override val y: Double,
        val ang: Double
) : Point(x, y)

class PathConfig {
    val waypoints = mutableListOf<Waypoint>()

    fun wp(x: Double, y: Double, ang: Double) {
        waypoints += Waypoint(x, y, ang)
    }

    infix fun Point.ang(ang: Double) = wp(x, y, ang)
}

data class PathSettings(
        val maxAcc: Double,
        val maxVel: Double,
        val trackWidth: Double
)

data class Path(val waypoints: List<Waypoint>)

fun runPath(settings: PathSettings, configure: PathConfig.() -> Unit) {
    val config = PathConfig().configure()

}

fun x() {
    runPath(0.0, 0.0) {
        p(0.0, 0.0) ang 0.0
    }
}
