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
open class CompositeUnit<OP : CompositionOperation, T1 : MetricUnitType, T2 : MetricUnitType>(
    val operation: OP,
    val unit1: MetricUnit<T1>,
    val unit2: MetricUnit<T2>
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

operator fun <T1 : MetricUnitType, T2 : MetricUnitType> MetricUnit<T1>.div(other: MetricUnit<T2>) =
        CompositeUnit(Per, this, other)

operator fun <T1 : MetricUnitType, T2 : MetricUnitType> MetricUnit<T1>.times(other: MetricUnit<T2>) =
        CompositeUnit(By, this, other)
