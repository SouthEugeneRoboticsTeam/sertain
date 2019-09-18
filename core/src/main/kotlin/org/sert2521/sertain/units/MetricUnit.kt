package org.sert2521.sertain.units

import kotlin.math.PI

abstract class MetricUnit<T : MetricUnitType>(val type: T, val base: Double, val symbol: String)

abstract class ChronicUnit(seconds: Double, symbol: String) : MetricUnit<Chronic>(Chronic, seconds, symbol)

object Seconds : ChronicUnit(1.0, " s")
object Minutes : ChronicUnit(60.0, " min")
object Milliseconds : ChronicUnit(0.001, " ms")

abstract class LinearUnit(meters: Double, symbol: String) : MetricUnit<Linear>(Linear, meters, symbol)

object Meters : LinearUnit(1.0, " m")
object Centimeters : LinearUnit(0.01, " cm")
object Millimeters : LinearUnit(0.001, " mm")

abstract class AngularUnit(radians: Double, symbol: String) : MetricUnit<Angular>(Angular, radians, symbol)

object Degrees : AngularUnit(PI / 180, "°")
object Radians : AngularUnit(1.0, " rad")
object Revolutions : AngularUnit(PI * 2, " rev")

abstract class LinearVelocityUnit(metersPerSecond: Double, symbol: String) : MetricUnit<LinearVelocity>(LinearVelocity, metersPerSecond, symbol)

object MetersPerSecond : LinearVelocityUnit(1.0, " m/s")
object MillimetersPerSecond : LinearVelocityUnit(0.001, " mm/s")

abstract class AngularVelocityUnit(radiansPerSecond: Double, symbol: String) : MetricUnit<AngularVelocity>(AngularVelocity, radiansPerSecond, symbol)

object RadiansPerSecond : AngularVelocityUnit(1.0, " rad/s")
object DegreesPerSecond : AngularVelocityUnit(PI / 180, "°/s")
object RevolutionsPerSecond : AngularVelocityUnit(PI * 2, " rev/s")
object RevolutionsPerMinute : AngularVelocityUnit((PI * 2) / 60, " rev/min")
