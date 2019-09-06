package org.sert2521.sertain.units

import java.lang.IllegalArgumentException

sealed class CompositionOperation

object Per : CompositionOperation()
object By : CompositionOperation()

class CompositeUnitType<OP : CompositionOperation, T1 : MetricUnitType, T2 : MetricUnitType>(
        operation: OP,
        type1: T1,
        type2: T2
) : MetricUnitType()

// By operation is not well tested!
class CompositeUnit<OP : CompositionOperation, T1 : MetricUnitType, T2 : MetricUnitType>(
        operation: OP,
        unit1: MetricUnit<T1>,
        unit2: MetricUnit<T2>
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
