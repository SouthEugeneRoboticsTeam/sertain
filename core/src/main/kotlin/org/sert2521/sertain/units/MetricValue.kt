package org.sert2521.sertain.units

import kotlin.math.ceil

data class MetricValue<T : MetricUnitType, U : MetricUnit<T>>(val unit: U, val value: Double) {
    override fun equals(other: Any?) = if (other is MetricValue<*, *> && unit.type == other.unit.type) {
        value * unit.base == other.value * other.unit.base
    } else {
        false
    }

    operator fun plus(other: MetricValue<T, U>) =
            MetricValue(unit, value + other.convertTo(unit).value)

    operator fun minus(other: MetricValue<T, U>) =
            MetricValue(unit, value - other.convertTo(unit).value)

    @Suppress("UNCHECKED_CAST")
    operator fun <T2 : MetricUnitType, U2 : MetricUnit<T2>> times(other: MetricValue<T2, U2>) =
        MetricValue(CompositeUnit(By, unit, other.unit), value * other.value)

    @Suppress("UNCHECKED_CAST")
    operator fun <T2 : MetricUnitType, U2 : MetricUnit<T2>> div(other: MetricValue<T2, U2>) =
        MetricValue(CompositeUnit(Per, unit, other.unit), value / other.value)

    operator fun <U : MetricUnit<T>> rem(other: MetricValue<T, U>) =
            MetricValue(unit, value % other.convertTo(unit).value)

    operator fun <U : MetricUnit<T>> compareTo(other: MetricValue<T, U>) =
            ceil(value * unit.base - other.value * other.unit.base).toInt()

    override fun hashCode(): Int {
        var result = unit.hashCode()
        result = 31 * result + value.hashCode()
        return result
    }

    override fun toString() = "$value${unit.symbol}"
}

typealias ChronicValue<U> = MetricValue<Chronic, U>
typealias LinearValue<U> = MetricValue<Linear, U>
typealias AngularValue<U> = MetricValue<Angular, U>
typealias VelocityValue<U1, U2> = MetricValue<Velocity, VelocityUnit<U1, U2>>
typealias AngularVelocityValue<U1, U2> = MetricValue<AngularVelocity, AngularVelocityUnit<U1, U2>>
typealias AccelerationValue<U1, U2> = MetricValue<Acceleration, AccelerationUnit<U1, U2>>
typealias AngularAccelerationValue<U1, U2> = MetricValue<AngularAcceleration, AngularAccelerationUnit<U1, U2>>

val Number.s: ChronicValue<Seconds> get() = MetricValue(Seconds, toDouble())
val Number.min: ChronicValue<Minutes> get() = MetricValue(Minutes, toDouble())
val Number.ms: ChronicValue<Milliseconds> get() = MetricValue(Milliseconds, toDouble())
val Number.m: LinearValue<Meters> get() = MetricValue(Meters, toDouble())
val Number.cm: LinearValue<Centimeters> get() = MetricValue(Centimeters, toDouble())
val Number.mm: LinearValue<Millimeters> get() = MetricValue(Millimeters, toDouble())
val Number.deg: AngularValue<Degrees> get() = MetricValue(Degrees, toDouble())
val Number.rad: AngularValue<Radians> get() = MetricValue(Radians, toDouble())
val Number.rev: AngularValue<Revolutions> get() = MetricValue(Revolutions, toDouble())
val Number.mps: VelocityValue<Meters, Seconds> get() = MetricValue(Meters / Seconds, toDouble())
val Number.mmps: VelocityValue<Millimeters, Seconds> get() = MetricValue(Millimeters / Seconds, toDouble())
val Number.dps: AngularVelocityValue<Degrees, Seconds> get() = MetricValue(Degrees / Seconds, toDouble())
val Number.rdps: AngularVelocityValue<Radians, Seconds> get() = MetricValue(Radians / Seconds, toDouble())
val Number.rps: AngularVelocityValue<Revolutions, Seconds> get() = MetricValue(Revolutions / Seconds, toDouble())
val Number.rpm: AngularVelocityValue<Revolutions, Minutes> get() = MetricValue(Revolutions / Minutes, toDouble())
val Number.mpss: AccelerationValue<Meters, Seconds> get() = MetricValue(Meters / (Seconds * Seconds), toDouble())
val Number.mmpss: AccelerationValue<Millimeters, Seconds> get() = MetricValue(Millimeters / (Seconds * Seconds), toDouble())
val Number.dpss: AngularAccelerationValue<Degrees, Seconds> get() = MetricValue(Degrees / (Seconds * Seconds), toDouble())
val Number.rdpss: AngularAccelerationValue<Radians, Seconds> get() = MetricValue(Radians / (Seconds * Seconds), toDouble())
val Number.rpss: AngularAccelerationValue<Revolutions, Seconds> get() = MetricValue(Revolutions / (Seconds * Seconds), toDouble())
val Number.rpmm: AngularAccelerationValue<Revolutions, Minutes> get() = MetricValue(Revolutions / (Minutes * Minutes), toDouble())
