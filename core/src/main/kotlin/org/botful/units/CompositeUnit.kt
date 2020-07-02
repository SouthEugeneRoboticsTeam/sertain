
package org.botful.units

sealed class CompositionOperation

object Per : CompositionOperation()
object By : CompositionOperation()

open class CompositeUnitType<OP : CompositionOperation, T1 : MetricUnitType, T2 : MetricUnitType>(
        val operation: OP,
        val type1: T1,
        val type2: T2
) : MetricUnitType()

typealias CompositeUnit<OP, T1, T2> = MetricUnit<CompositeUnitType<OP, T1, T2>>
fun <OP : CompositionOperation, T1: MetricUnitType, T2: MetricUnitType, U1 : MetricUnit<T1>, U2 : MetricUnit<T2>> compositeUnit(operation: OP, unit1: U1, unit2: U2) = CompositeUnit(
        CompositeUnitType(operation, unit1.type, unit2.type),
        when (operation) {
            Per -> unit1.base / unit2.base
            By -> unit1.base * unit2.base
            else -> throw IllegalArgumentException("Unsupported unit operation")
        },
        when (operation) {
            Per -> if (unit1 != unit2) {
                " ${unit1.symbol.trim()}/${unit2.symbol.trim()}"
            } else {
                ""
            }
            By -> if (unit1 != unit2) {
                " ${unit1.symbol.trim()}-${unit2.symbol.trim()}"
            } else {
                " ${unit1.symbol.trim()}²"
            }
            else -> throw IllegalArgumentException("Unsupported unit operation")
        }
)

operator fun <T1 : MetricUnitType, T2 : MetricUnitType> MetricUnit<T1>.div(other: MetricUnit<T2>) =
        compositeUnit(Per, this, other)

operator fun <T1 : MetricUnitType, T2 : MetricUnitType> MetricUnit<T1>.times(other: MetricUnit<T2>) =
        compositeUnit(By, this, other)

typealias Velocity = CompositeUnitType<Per, Linear, Chronic>
typealias AngularVelocity = CompositeUnitType<Per, Angular, Chronic>
typealias Acceleration = CompositeUnitType<Per, Linear, CompositeUnitType<By, Chronic, Chronic>>
typealias AngularAcceleration = CompositeUnitType<Per, Angular, CompositeUnitType<By, Chronic, Chronic>>

typealias VelocityUnit = MetricUnit<Velocity>
typealias AngularVelocityUnit = MetricUnit<AngularVelocity>
typealias AccelerationUnit = MetricUnit<Acceleration>
typealias AngularAccelerationUnit = MetricUnit<AngularAcceleration>
