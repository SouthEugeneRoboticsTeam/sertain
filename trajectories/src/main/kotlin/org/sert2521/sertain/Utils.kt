package org.sert2521.sertain

import org.sert2521.sertain.pathfinding.Point
import org.sert2521.sertain.pathfinding.Segment
import kotlin.math.*

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

fun Double.root(n: Int): Double {
    if (n < 2) throw IllegalArgumentException("n must be more than 1")
    if (this <= 0.0) throw IllegalArgumentException("must be positive")
    val np = n - 1
    fun iter(g: Double) = (np * g + this / g.pow(np.toDouble())) / n
    var g1 = this
    var g2 = iter(g1)
    while (g1 != g2) {
        g1 = iter(g1)
        g2 = iter(iter(g2))
    }
    return g1
}

fun Double.roundTo(decimalPlaces: Int) =
        round(this * 10.0.pow(decimalPlaces)) / 10.0.pow(decimalPlaces)

fun boundPercent(percentage: Double) = max(min(percentage, 1.0), 0.0)
