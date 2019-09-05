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
