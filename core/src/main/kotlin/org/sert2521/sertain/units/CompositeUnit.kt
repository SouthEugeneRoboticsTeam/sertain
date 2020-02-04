package org.sert2521.sertain.units

sealed class CompositionOperation

object Per : CompositionOperation()
object By : CompositionOperation()

open class CompositeUnitType<OP : CompositionOperation, T1 : MetricUnitType, T2 : MetricUnitType>(
        val operation: OP,
        val type1: T1,
        val type2: T2
) : MetricUnitType()

// By operation is not well tested!
open class CompositeUnit<
        OP : CompositionOperation,
        T1 : MetricUnitType,
        T2 : MetricUnitType,
        U1 : MetricUnit<T1>,
        U2 : MetricUnit<T2>
>(
        val operation: OP,
        val unit1: U1,
        val unit2: U2
) : MetricUnit<CompositeUnitType<OP, T1, T2>>(
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

operator fun <T1 : MetricUnitType, T2 : MetricUnitType, U1 : MetricUnit<T1>, U2 : MetricUnit<T2>> U1.div(other: U2) =
        CompositeUnit(Per, this, other)

operator fun <T1 : MetricUnitType, T2 : MetricUnitType, U1 : MetricUnit<T1>, U2 : MetricUnit<T2>> U1.times(other: U2) =
        CompositeUnit(By, this, other)

typealias Velocity = CompositeUnitType<Per, Linear, Chronic>
typealias AngularVelocity = CompositeUnitType<Per, Angular, Chronic>
typealias Acceleration = CompositeUnitType<Per, Linear, CompositeUnitType<By, Chronic, Chronic>>
typealias AngularAcceleration = CompositeUnitType<Per, Angular, CompositeUnitType<By, Chronic, Chronic>>

typealias VelocityUnit<U1, U2> = CompositeUnit<Per, Linear, Chronic, U1, U2>
typealias AngularVelocityUnit<U1, U2> = CompositeUnit<Per, Angular, Chronic, U1, U2>
typealias AccelerationUnit<U1, U2> = CompositeUnit<Per, Linear, CompositeUnitType<By, Chronic, Chronic>, U1, CompositeUnit<By, Chronic, Chronic, U2, U2>>
typealias AngularAccelerationUnit<U1, U2> = CompositeUnit<Per, Angular, CompositeUnitType<By, Chronic, Chronic>, U1, CompositeUnit<By, Chronic, Chronic, U2, U2>>
