package org.sert2521.sertain.units

import kotlin.math.PI

open class MetricUnit<T : MetricUnitType>(val type: T, val base: Double, val symbol: String)

// abstract class ChronicUnit(seconds: Double, symbol: String) : MetricUnit<Chronic>(Chronic, seconds, symbol)
typealias ChronicUnit = MetricUnit<Chronic>
fun chronicUnit(seconds: Double, symbol: String): ChronicUnit = MetricUnit(Chronic, seconds, symbol)

val seconds = chronicUnit(1.0, " s")
val minutes =  chronicUnit(60.0, " min")
val milliseconds = chronicUnit(0.001, " ms")

typealias LinearUnit = MetricUnit<Linear>
fun linearUnit(meters: Double, symbol: String): LinearUnit = MetricUnit(Linear, meters, symbol)

val meters = linearUnit(1.0, " m")
val centimeters = linearUnit(0.01, " cm")
val millimeters = linearUnit(0.001, " mm")
val feet = linearUnit(0.3048, " ft")
val inches = linearUnit(0.0254, " in")

typealias AngularUnit = MetricUnit<Angular>
fun angularUnit(radians: Double, symbol: String): AngularUnit = MetricUnit(Angular, radians, symbol)

val degrees = angularUnit(PI / 180, "°")
val radians = angularUnit(1.0, " rad")
val revolutions = angularUnit(PI * 2, " rev")

