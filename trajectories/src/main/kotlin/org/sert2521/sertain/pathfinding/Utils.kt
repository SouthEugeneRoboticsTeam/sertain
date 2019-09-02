package org.sert2521.sertain.pathfinding

import kotlin.math.PI
import kotlin.math.abs
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

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

class CachedValue<T>(val get: () -> T) : ReadOnlyProperty<Any?, T> {
    val value: T = get()

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {

    }
}
