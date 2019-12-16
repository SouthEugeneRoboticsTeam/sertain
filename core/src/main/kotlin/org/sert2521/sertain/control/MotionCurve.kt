package org.sert2521.sertain.control

import org.sert2521.sertain.utils.root
import org.sert2521.sertain.utils.roundTo
import kotlin.math.*

data class MotionCurveConfig(
        val distance: Double,
        val maxVelocity: Double,
        val maxAcceleration: Double,
        val maxJerk: Double
)

class MotionCurve(config: MotionCurveConfig) {
    val d = config.distance
    val vm = config.maxVelocity
    val am = config.maxAcceleration
    val j = config.maxJerk
    val v = min(
            vm,
            if (am > ((j.pow(2) * d) / 2).root(3)) {
                ((j.pow(2) * d) / 2).root(3).pow(2) / j
            } else {
                (-am.pow(2) + sqrt(am.pow(4) + 4 * j.pow(2) * d * am)) / (2 * j)
            }
    )
    val a = min(
            am,
            if (vm > ((j.pow(2) * d) / 2).root(3).pow(2) / j) {
                ((j.pow(2) * d) / 2).root(3)
            } else {
                sqrt(vm * j)
            }
    )

    val t1 = a / j
    val t2 = v / a
    val t3 = t1 + t2
    val t4 = d / v
    val t5 = t1 + t4
    val t6 = t2 + t4
    val t7 = t3 + t4

    val isValid = (
            d >= 0 && vm >= 0 && am >= 0 && j >= 0
                    && t2.roundTo(4) - t1.roundTo(4) >= 0
                    && t4.roundTo(4) - t3.roundTo(4) >= 0
            )

    private fun j1() = j
    private fun j2() = 0.0
    private fun j3() = -j
    private fun j4() = 0.0
    private fun j5() = -j
    private fun j6() = 0.0
    private fun j7() = j

    private fun a1(t: Double) = j1() * t
    private fun a2() = a
    private fun a3(t: Double) = a2() + j3() * (t - t2)
    private fun a4() = 0.0
    private fun a5(t: Double) = j5() * (t - t4)
    private fun a6() = -a
    private fun a7(t: Double) = a6() + j7() * (t - t6)

    private fun v1(t: Double) = 0.5 * j1() * t.pow(2)
    private fun v2(t: Double) = v1(t1) + a1(t1) * (t - t1)
    private fun v3(t: Double) = v2(t2) + a2() * (t - t2) + 0.5 * j3() * (t - t2).pow(2)
    private fun v4() = v
    private fun v5(t: Double) = v4() + a4() * (t - t4) + 0.5 * j5() * (t - t4).pow(2)
    private fun v6(t: Double) = v5(t5) + a5(t5) * (t - t5)
    private fun v7(t: Double) = v6(t6) + a6() * (t - t6) + 0.5 * j7() * (t - t6).pow(2)

    private fun d1(t: Double) = (1.0 / 6.0) * j1() * t.pow(3)
    private fun d2(t: Double) = d1(t1) + v1(t1) * (t - t1) + 0.5 * a1(t1) * (t - t1).pow(2)
    private fun d3(t: Double) = d2(t2) + v2(t2) * (t - t2) + 0.5 * a2() * (t - t2).pow(2) + (1.0 / 6.0) * j3() * (t - t2).pow(3)
    private fun d4(t: Double) = d3(t3) + v3(t3) * (t - t3)
    private fun d5(t: Double) = d4(t4) + v4() * (t - t4) + 0.5 * a4() * (t - t4).pow(2) + (1.0 / 6.0) * j5() * (t - t4).pow(3)
    private fun d6(t: Double) = d5(t5) + v5(t5) * (t - t5) + 0.5 * a5(t5) * (t - t5).pow(2)
    private fun d7(t: Double) = d6(t6) + v6(t6) * (t - t6) + 0.5 * a6() * (t - t6).pow(2) + (1.0 / 6.0) * j7() * (t - t6).pow(3)

    fun j(t: Double) = when {
        t < 0 -> throw IllegalArgumentException("t cannot be negative")
        t <= t1 -> j1()
        t <= t2 -> j2()
        t <= t3 -> j3()
        t <= t4 -> j4()
        t <= t5 -> j5()
        t <= t6 -> j6()
        t <= t7 -> j7()
        else -> throw IllegalArgumentException("t cannot be greater than t7")
    }

    fun a(t: Double) = when {
        t < 0 -> throw IllegalArgumentException("t cannot be negative")
        t <= t1 -> a1(t)
        t <= t2 -> a2()
        t <= t3 -> a3(t)
        t <= t4 -> a4()
        t <= t5 -> a5(t)
        t <= t6 -> a6()
        t <= t7 -> a7(t)
        else -> throw IllegalArgumentException("t cannot be greater than t7")
    }

    fun v(t: Double) = when {
        t < 0 -> throw IllegalArgumentException("t cannot be negative")
        t <= t1 -> v1(t)
        t <= t2 -> v2(t)
        t <= t3 -> v3(t)
        t <= t4 -> v4()
        t <= t5 -> v5(t)
        t <= t6 -> v6(t)
        t <= t7 -> v7(t)
        else -> throw IllegalArgumentException("t cannot be greater than t7")
    }

    fun d(t: Double) = when {
        t < 0 -> throw IllegalArgumentException("t cannot be negative")
        t <= t1 -> d1(t)
        t <= t2 -> d2(t)
        t <= t3 -> d3(t)
        t <= t4 -> d4(t)
        t <= t5 -> d5(t)
        t <= t6 -> d6(t)
        t <= t7 -> d7(t)
        else -> throw IllegalArgumentException("t cannot be greater than t7")
    }
}
