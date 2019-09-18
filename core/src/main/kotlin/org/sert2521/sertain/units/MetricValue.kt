package org.sert2521.sertain.units

import kotlin.math.ceil

data class MetricValue<T : MetricUnitType, U : MetricUnit<T>>(val unit: U, val value: Double) {
    override fun equals(other: Any?) = if (other is MetricValue<*, *> && unit.type == other.unit.type) {
        value * unit.base == other.value * other.unit.base
    } else {
        false
    }

    operator fun <S : MetricUnit<T>> plus(other: MetricValue<T, S>) =
            MetricValue(unit, value + other.convertTo(unit).value)

    operator fun <S : MetricUnit<T>> minus(other: MetricValue<T, S>) =
            MetricValue(unit, value - other.convertTo(unit).value)

    @Suppress("UNCHECKED_CAST")
    operator fun <R : MetricUnitType, S : MetricUnit<R>> times(other: MetricValue<*, S>) = if (unit.type == other.unit.type) {
        MetricValue(CompositeUnit(By, unit, unit), value * (other as MetricValue<T, U>).convertTo(unit).value)
    } else {
        MetricValue(CompositeUnit(By, unit, other.unit), value * other.value)
    }

    @Suppress("UNCHECKED_CAST")
    operator fun <R : MetricUnitType, S : MetricUnit<R>> div(other: MetricValue<*, S>) = if (unit.type == other.unit.type) {
        MetricValue(CompositeUnit(Per, unit, unit), value / (other as MetricValue<T, U>).convertTo(unit).value)
    } else {
        MetricValue(CompositeUnit(Per, unit, other.unit), value / other.value)
    }

    operator fun <S : MetricUnit<T>> rem(other: MetricValue<T, S>) =
            MetricValue(unit, value % other.convertTo(unit).value)

    operator fun <S : MetricUnit<T>> compareTo(other: MetricValue<T, S>) =
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
