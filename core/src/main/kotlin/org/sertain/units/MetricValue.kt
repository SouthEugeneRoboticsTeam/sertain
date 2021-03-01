package org.sertain.units

import kotlin.math.ceil

data class MetricValue<T : MetricUnitType>(val unit: MetricUnit<T>, val value: Double) {
    override fun equals(other: Any?) = if (other is MetricValue<*> && unit.type == other.unit.type) {
        value * unit.base == other.value * other.unit.base
    } else {
        false
    }

    operator fun <S : MetricUnit<T>> plus(other: MetricValue<T>) =
            MetricValue(unit, value + other.convertTo(unit).value)

    operator fun <S : MetricUnit<T>> minus(other: MetricValue<T>) =
            MetricValue(unit, value - other.convertTo(unit).value)

    @Suppress("UNCHECKED_CAST")
    operator fun <T2 : MetricUnitType> times(other: MetricValue<T2>) = if (unit.type == other.unit.type) {
        MetricValue(compositeUnit(By, unit, unit), value * (other as MetricValue<T>).convertTo(unit).value)
    } else {
        MetricValue(compositeUnit(By, unit, other.unit), value * other.value)
    }

    @Suppress("UNCHECKED_CAST")
    operator fun <T2 : MetricUnitType> div(other: MetricValue<T2>) = if (unit.type == other.unit.type) {
        MetricValue(compositeUnit(Per, unit, unit), value / (other as MetricValue<T>).convertTo(unit).value)
    } else {
        MetricValue(compositeUnit(Per, unit, other.unit), value / other.value)
    }

    operator fun <S : MetricUnit<T>> rem(other: MetricValue<T>) =
            MetricValue(unit, value % other.convertTo(unit).value)

    operator fun <S : MetricUnit<T>> compareTo(other: MetricValue<T>) =
            ceil(value * unit.base - other.value * other.unit.base).toInt()

    override fun hashCode(): Int {
        var result = unit.hashCode()
        result = 31 * result + value.hashCode()
        return result
    }

    override fun toString() = "$value${unit.symbol}"
}

val Number.s get() = MetricValue(seconds, toDouble())
val Number.min get() = MetricValue(minutes, toDouble())
val Number.ms get() = MetricValue(milliseconds, toDouble())
val Number.m get() = MetricValue(meters, toDouble())
val Number.cm get() = MetricValue(centimeters, toDouble())
val Number.mm get() = MetricValue(millimeters, toDouble())
val Number.ft get() = MetricValue(feet, toDouble())
val Number.inch get() = MetricValue(feet, toDouble())
val Number.deg get() = MetricValue(degrees, toDouble())
val Number.rad get() = MetricValue(radians, toDouble())
val Number.rev get() = MetricValue(revolutions, toDouble())
val Number.mps: MetricValue<Velocity> get() = MetricValue(meters / seconds, toDouble())
val Number.mmps: MetricValue<Velocity> get() = MetricValue(millimeters / seconds, toDouble())
val Number.dps: MetricValue<AngularVelocity> get() = MetricValue(degrees / seconds, toDouble())
val Number.rdps: MetricValue<AngularVelocity> get() = MetricValue(radians / seconds, toDouble())
val Number.rps: MetricValue<AngularVelocity> get() = MetricValue(revolutions / seconds, toDouble())
val Number.rpm: MetricValue<AngularVelocity> get() = MetricValue(revolutions / minutes, toDouble())
val Number.mpss: MetricValue<Acceleration> get() = MetricValue(meters / (seconds * seconds), toDouble())
val Number.mmpss: MetricValue<Acceleration> get() = MetricValue(millimeters / (seconds * seconds), toDouble())
val Number.dpss: MetricValue<AngularAcceleration> get() = MetricValue(degrees / (seconds * seconds), toDouble())
val Number.rdpss: MetricValue<AngularAcceleration> get() = MetricValue(radians / (seconds * seconds), toDouble())
val Number.rpss: MetricValue<AngularAcceleration> get() = MetricValue(revolutions / (seconds * seconds), toDouble())
val Number.rpmm: MetricValue<AngularAcceleration> get() = MetricValue(revolutions / (minutes * minutes), toDouble())
