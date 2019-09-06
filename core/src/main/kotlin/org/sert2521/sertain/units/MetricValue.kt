package org.sert2521.sertain.units

import kotlin.math.ceil

data class MetricValue<T : MetricUnitType, U : MetricUnit<T>>(val unit: U, val value: Double) {
    override fun equals(other: Any?) = if (other is MetricValue<*, *> && unit.type == other.unit.type) {
        value * unit.base == other.value * other.unit.base
    } else {
        false
    }

    @Suppress("UNCHECKED_CAST")
    operator fun <R : T, S : MetricUnit<R>> plus(other: MetricValue<R, S>) =
            MetricValue(unit, value + other.convertTo(unit as MetricUnit<R>).value)

    @Suppress("UNCHECKED_CAST")
    operator fun <R : T, S : MetricUnit<R>> minus(other: MetricValue<R, S>) =
            MetricValue(unit, value - other.convertTo(unit as MetricUnit<R>).value)

    @Suppress("UNCHECKED_CAST")
    operator fun <R : T, S : MetricUnit<R>> times(other: MetricValue<R, S>) =
            MetricValue(CompositeUnit(By, unit, unit), value * other.convertTo(unit as MetricUnit<R>).value)

    @Suppress("UNCHECKED_CAST")
    operator fun <R : T, S : MetricUnit<R>> div(other: MetricValue<R, S>) =
            value / other.convertTo(unit as MetricUnit<R>).value

    @Suppress("UNCHECKED_CAST")
    operator fun <R : T, S : MetricUnit<R>> rem(other: MetricValue<R, S>) =
            MetricValue(unit, value % other.convertTo(unit as MetricUnit<R>).value)

    operator fun <R : T, S : MetricUnit<R>> compareTo(other: MetricValue<R, S>) =
            ceil(value * unit.base - other.value * other.unit.base).toInt()

    override fun hashCode(): Int {
        var result = unit.hashCode()
        result = 31 * result + value.hashCode()
        return result
    }

    override fun toString() = "$value${unit.symbol}"
}

val Number.s get() = MetricValue(Seconds, toDouble())
val Number.min get() = MetricValue(Minutes, toDouble())
val Number.ms get() = MetricValue(Milliseconds, toDouble())
val Number.m get() = MetricValue(Meters, toDouble())
val Number.cm get() = MetricValue(Centimeters, toDouble())
val Number.mm get() = MetricValue(Millimeters, toDouble())
val Number.deg get() = MetricValue(Degrees, toDouble())
val Number.rad get() = MetricValue(Radians, toDouble())
val Number.rev get() = MetricValue(Revolutions, toDouble())
val Number.mps get() = MetricValue(MetersPerSecond, toDouble())
val Number.mmps get() = MetricValue(MillimetersPerSecond, toDouble())
val Number.dps get() = MetricValue(DegreesPerSecond, toDouble())
val Number.rdps get() = MetricValue(RadiansPerSecond, toDouble())
val Number.rps get() = MetricValue(RevolutionsPerSecond, toDouble())
val Number.rpm get() = MetricValue(RevolutionsPerMinute, toDouble())
