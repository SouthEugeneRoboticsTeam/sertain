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

// Math is not well tested!
class CompositeUnit<OP : CompositionOperation, T1 : MetricUnitType, T2 : MetricUnitType, T : CompositeUnitType<OP, T1, T2>>(
        operation: OP,
        unit1: MetricUnit<T1>,
        unit2: MetricUnit<T2>
) : MetricUnit<CompositeUnitType<OP, T1, T2>>(
        CompositeUnitType(operation, unit1.type, unit2.type),
        when (operation) {
            Per -> unit1.base * unit2.base
            By -> unit1.base / unit2.base
            else -> throw IllegalArgumentException("Unsupported unit operation")
        }
)
