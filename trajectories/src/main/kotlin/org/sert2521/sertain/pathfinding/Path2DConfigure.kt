package org.sert2521.sertain.pathfinding

class Path2DConfigure(val name: String = "ANONYMOUS_PATH") {
    val waypoints = mutableListOf<WayPoint>()

    var dt = 0.0
    var maxVelocity = 0.0
    var maxAcceleration = 0.0
    var maxJerk = 0.0

    infix fun Point.ang(angle: Double) {
        waypoints += WayPoint(x, y, angle)
    }
}
