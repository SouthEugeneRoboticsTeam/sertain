package org.sert2521.sertain.networktables

import edu.wpi.first.networktables.NetworkTable as WpiNetworkTable
import edu.wpi.first.networktables.NetworkTableInstance
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class TableEntry<T>(val value: T, val location: List<String>) : ReadWriteProperty<Any?, T> {
    var initialized = false

    private val wpiTable: WpiNetworkTable get() {
        var table = NetworkTableInstance.getDefault().getTable(location.first())
        location.drop(1).dropLast(0).forEach {
            table = table.getSubTable(it)
        }
        return table
    }

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        if (!initialized) {
            setValue(thisRef, property, value)
            initialized = true
        }
        return wpiTable.getEntry(property.name).value.value as T
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        wpiTable.getEntry(property.name).setValue(value)
    }
}
