package org.sert2521.sertain.animations

import org.sert2521.sertain.root
import org.sert2521.sertain.roundTo
import kotlin.math.*

data class SCurveConfig(
        val distance: Int,
        val maxVelocity: Double,
        val maxAcceleration: Double,
        val maxJerk: Double
)

class SCurveAnimation(config: SCurveConfig) {
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
}
