package org.sert2521.sertain.units

class MetricValue<T : MetricUnitType, U : MetricUnit<T>>(val unit: U, val value: Double)

val Number.s get() = MetricValue(Seconds, toDouble())
val Number.ms get() = MetricValue(Milliseconds, toDouble())
val Number.m get() = MetricValue(Meters, toDouble())
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
