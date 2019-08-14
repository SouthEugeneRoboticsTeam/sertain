package org.sert2521.sertain.units

import kotlin.math.PI

abstract class MetricUnit<T : MetricUnitType>(val type: T, val base: Double)

abstract class ChronicUnit(seconds: Double) : MetricUnit<Chronic>(Chronic, seconds)

object Seconds : ChronicUnit(1.0)
object Minutes : ChronicUnit(60.0)
object Milliseconds : ChronicUnit(0.001)

abstract class LinearUnit(meters: Double) : MetricUnit<Linear>(Linear, meters)

object Meters : LinearUnit(1.0)
object Millimeters : LinearUnit(0.001)

abstract class AngularUnit(radians: Double) : MetricUnit<Angular>(Angular, radians)

object Degrees : AngularUnit(PI / 180)
object Radians : AngularUnit(1.0)
object Revolutions : AngularUnit(PI * 2)

abstract class LinearVelocityUnit(metersPerSecond: Double) : MetricUnit<LinearVelocity>(LinearVelocity, metersPerSecond)

object MetersPerSecond : LinearVelocityUnit(1.0)
object MillimetersPerSecond : LinearVelocityUnit(0.001)

abstract class AngularVelocityUnit(radiansPerSecond: Double) : MetricUnit<AngularVelocity>(AngularVelocity, radiansPerSecond)

object RadiansPerSecond : LinearVelocityUnit(1.0)
object DegreesPerSecond : LinearVelocityUnit(PI / 180)
object RevolutionsPerSecond : LinearVelocityUnit(PI * 2)
object RevolutionsPerMinute : LinearVelocityUnit((PI * 2) / 60)
