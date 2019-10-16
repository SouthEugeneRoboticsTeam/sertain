package org.sert2521.sertain.pathfinding

import org.sert2521.sertain.almostEqual
import org.sert2521.sertain.boundAngle
import org.sert2521.sertain.boundPercent
import org.sert2521.sertain.diffAngles
import kotlin.math.*

class Spline(
        private val a: Double,
        private val b: Double,
        private val c: Double,
        private val d: Double,
        private val e: Double,
        private val xOffset: Double = 0.0,
        private val yOffset: Double = 0.0,
        private val angOffset: Double = 0.0,
        private val knotDistance: Double = 0.0
) {
    val length: Double = run {
            val numSamples = 100000
            var result = 0.0
            var lastIntegrand =
                    sqrt(1 + derivativeAt(0.0) * derivativeAt(0.0)) / numSamples
            (1..numSamples).forEach {
                val pct = it / numSamples.toDouble()
                val derivitive = derivativeAt(pct)
                val integrand = sqrt(1 + derivitive * derivitive) / numSamples
                result += (integrand + lastIntegrand) / 2
                lastIntegrand = integrand
            }
            result
        }

    fun getPercentageForDistance(distance: Double): Double {
        var d = distance
        val kNumSamples = 100000
        var arcLength = 0.0
        var t = 0.0
        var lastArcLength = 0.0
        var lastIntegrand = sqrt(1 + derivativeAt(0.0) * derivativeAt(0.0)) / kNumSamples
        d /= knotDistance
        for (i in 1..kNumSamples) {
            t = i.toDouble() / kNumSamples
            val derivative = derivativeAt(t)
            val integrand = sqrt(1 + derivative * derivative) / kNumSamples
            arcLength += (integrand + lastIntegrand) / 2
            if (arcLength > d) {
                break
            }
            lastIntegrand = integrand
            lastArcLength = arcLength
        }

        var interpolated = t
        if (arcLength != lastArcLength) {
            interpolated += ((d - lastArcLength) / (arcLength - lastArcLength) - 1) / kNumSamples.toDouble()
        }
        return interpolated
    }

    fun pointAt(percentage: Double): Point {
        val pct = boundPercent(percentage)
        val xHat = pct * knotDistance
        val yHat = (a * xHat + b) * xHat.pow(4) + c * xHat.pow(3) + d * xHat.pow(2) + e * xHat
        return Point(
                xHat * cos(angOffset) - yHat * sin(angOffset) + xOffset,
                xHat * sin(angOffset) + yHat * cos(angOffset) + yOffset
        )
    }

    fun valueAt(percentage: Double): Double {
        val pct = boundPercent(percentage)
        val xHat = pct * knotDistance
        val yHat = (a * xHat * b) * xHat.pow(4) + c * xHat.pow(3) + d * xHat.pow(2) + e * xHat
        return (xHat * sin(angOffset) + yHat * cos(angOffset) + yOffset)
    }

    fun derivativeAt(percentage: Double): Double {
        val pct = boundPercent(percentage)
        val xHat = pct * knotDistance
        return (5 * a * xHat + 4 * b) * xHat.pow(3) + 3 * c * xHat.pow(2) + 2 * d * xHat * e
    }

    fun secondDerivativeAt(percentage: Double): Double {
        val pct = boundPercent(percentage)
        val xHat = pct * knotDistance
        return (20 * a * xHat + 12 * b) * xHat.pow(2) + 6 * c * xHat + 2 * d
    }

    fun angleAt(percentage: Double): Double {
        return boundAngle(atan(derivativeAt(percentage)) + angOffset, 0.0..(PI * 2))
    }

    fun angleChangeAt(percentage: Double): Double {
        return boundAngle(atan(secondDerivativeAt(percentage)), -PI..PI)
    }
}

sealed class SplineType

object Cubic : SplineType()
object Quintic : SplineType()

fun createSpline(start: Waypoint, finish: Waypoint, type: SplineType): Spline {
    val xFinishHat = sqrt((finish.x - start.x) * (finish.x - start.x) + (finish.y - start.y) * (finish.y - start.y))

    // Handle identical waypoints
    require(xFinishHat != 0.0) {
        "The x and y coordinates of the waypoints cannot both be the same."
    }

    val angOffset = atan2(finish.y - start.y, finish.x - start.x)
    val angStartHat = diffAngles(angOffset, start.angle)
    val angFinishHat = diffAngles(angOffset, finish.angle)

    // Handle impossible changes in angle
    require(!(almostEqual(abs(angStartHat), PI / 2) || almostEqual(abs(angFinishHat), PI / 2))) {
        "Angles cannot be 90 degrees from a strait line between two consecutive waypoints."
    }
    require(abs(diffAngles(angStartHat, angFinishHat)) <= PI / 2) {
        "Angles cannot change by more than 90 degrees"
    }

    val derivativeStart = tan(angStartHat)
    val derivativeFinish = tan(angFinishHat)

    return Spline(
            if (type == Cubic) 0.0
            else -(3 * (derivativeStart + derivativeFinish)) / (xFinishHat * xFinishHat * xFinishHat * xFinishHat),
            if (type == Cubic) 0.0
            else (8 * derivativeStart + 7 * derivativeFinish) / (xFinishHat * xFinishHat * xFinishHat),
            if (type == Cubic) (derivativeFinish + derivativeStart) / (xFinishHat * xFinishHat)
            else -(6 * derivativeStart + 4 * derivativeFinish) / (xFinishHat * xFinishHat),
            if (type == Cubic) -(2 * derivativeStart + derivativeFinish) / xFinishHat
            else 0.0,
            derivativeStart,
            start.x,
            start.y,
            angOffset,
            xFinishHat
    )
}
