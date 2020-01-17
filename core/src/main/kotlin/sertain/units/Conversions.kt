package org.sert2521.sertain.units

fun <T : MetricUnitType, U1 : MetricUnit<T>, U2 : MetricUnit<T>> Double.convert(units: Pair<U1, U2>): Double {
    return this * units.first.base / units.second.base
}

fun <T : MetricUnitType, U1 : MetricUnit<T>, U2 : MetricUnit<T>> MetricValue<T, U1>.convertTo(other: U2): MetricValue<T, U2> {
    return MetricValue(other, value.convert(unit to other))
}
