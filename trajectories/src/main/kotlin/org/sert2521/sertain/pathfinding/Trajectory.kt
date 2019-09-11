package org.sert2521.sertain.pathfinding

import org.sert2521.sertain.factor

class Trajectory(segments: List<Segment>, val inverted: Boolean = false, val factor: Double = 1.0) {
    val segments = SegmentCollection(segments, inverted, factor)

    val size get() = segments.size

    fun toList() = segments.toList()

    operator fun get(index: Int) = segments[index]

    fun clone() = Trajectory(segments.toList(), inverted, factor)

    operator fun plus(trajectory: Trajectory) =
            Trajectory(segments.toList() + List(trajectory.size) { trajectory[it] }, inverted, factor)
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

class SegmentCollection(private val segments: List<Segment>, val inverted: Boolean, val factor: Double) {
    operator fun get(index: Int) = if (!inverted)
        segments[index].factor(factor, factor, factor, factor, factor, factor, factor, factor)
    else {
        segments[index].factor(factor, factor, factor, factor, -factor, factor, factor, -factor)
    }

    val size get() = segments.size

    fun toList() = segments
}
