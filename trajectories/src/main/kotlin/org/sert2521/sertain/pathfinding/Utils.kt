package org.sert2521.sertain.pathfinding

import kotlin.math.PI

fun almostEqual(a: Double, b: Double) = Math.abs(a - b) < 1E-6

fun diffAngles(a: Double, b: Double) = boundAngle(a - b, -PI..PI)

fun boundAngle(angle: Double, bounds: ClosedRange<Double>): Double {
    var result = angle
    while(result < bounds.start) {
        result += PI * 2
    }
    while (result > bounds.endInclusive) {
        result -= PI * 2
    }
    return result
}

fun p(x: Double, y: Double) = Point(x, y)

fun Segment.factor(
        pos: Double = 1.0,
        vel: Double = 1.0,
        acc: Double = 1.0,
        jerk: Double = 1.0,
        ang: Double = 1.0,
        dt: Double = 1.0,
        x: Double = 1.0,
        y: Double = 1.0
) = copy(
        pos = this.pos * pos,
        vel = this.vel * vel,
        acc = this.acc * acc,
        jerk = this.jerk * jerk,
        ang = this.ang * ang,
        dt = this.dt * dt,
        x = this.x * x,
        y = this.y * y
)
