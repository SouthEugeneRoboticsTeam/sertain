package org.sert2521.sertain.telemetry

import edu.wpi.first.networktables.NetworkTable
import edu.wpi.first.networktables.NetworkTableInstance

fun wpiTable(vararg location: String): NetworkTable {
    var table = NetworkTableInstance.getDefault().getTable(location.first())
    location.drop(1).dropLast(0).forEach {
        table = table.getSubTable(it)
    }
    return table
}

class TableEntry<T>(val name: String, initialValue: T, val parent: Table) {
    constructor(name: String, initialValue: T, vararg location: String) :
            this(name, initialValue, if (location.isNotEmpty()) Table(location.last(), *location.dropLast(1).toTypedArray()) else Table("Global"))

    val location: List<String> = parent.location + parent.name

    init {
        wpiTable(*location.toTypedArray()).getEntry(name).setValue(initialValue)
    }

    val wpiProperty get() = wpiTable(*location.toTypedArray()).getEntry(name)

    @Suppress("UNCHECKED_CAST")
    var value: T
        get() = wpiProperty.value.value as T
        set(value) {
            wpiProperty.setValue(value)
        }
}

fun groundControlToMajorTom() = Unit
