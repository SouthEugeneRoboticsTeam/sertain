package org.sert2521.sertain.telemetry

import edu.wpi.first.networktables.NetworkTable
import edu.wpi.first.networktables.NetworkTableInstance

fun wpiTable(location: List<String>): NetworkTable {
    var table = NetworkTableInstance.getDefault().getTable(location.first())
    location.drop(1).dropLast(0).forEach {
        table = table.getSubTable(it)
    }
    return table
}

class TableEntry<T>(val name: String, initialValue: T, val location: List<String> = emptyList()) {
    init {
        wpiTable(location).getEntry(name).setValue(initialValue)
    }

    val wpiProperty get() = wpiTable(location).getEntry(name)

    @Suppress("UNCHECKED_CAST")
    var value: T
        get() = wpiProperty.value.value as T
        set(value) {
            wpiProperty.setValue(value)
        }
}

fun groundControlToMajorTom() = Unit
