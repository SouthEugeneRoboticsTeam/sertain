package org.sert2521.sertain.pathfinding

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
