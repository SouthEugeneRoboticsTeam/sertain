package org.sert2521.sertain.telemetry

import edu.wpi.first.networktables.NetworkTable
import edu.wpi.first.networktables.NetworkTableInstance
import edu.wpi.first.networktables.TableEntryListener
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

private fun wpiTable(vararg location: String): NetworkTable {
    var table = NetworkTableInstance.getDefault().getTable(location.first())
    location.drop(1).dropLast(0).forEach {
        table = table.getSubTable(it)
    }
    return table
}

class TableEntry<T> private constructor(parent: Table, val name: String, initialValue: T) : ReadWriteProperty<Any?, T> {
    enum class Type {
        STRING, NUMBER, BOOLEAN
    }

    val location = parent.location + parent.name

    private val wpiTable = wpiTable(*location.toTypedArray())
    private val wpiProperty get() = wpiTable(*location.toTypedArray()).getEntry(name)

    init {
        wpiTable().getEntry("").createRpc {  }
        wpiProperty.setValue(value)
    }

    @Suppress("UNCHECKED_CAST")
    var value: T
        get() = wpiProperty.value.value as T
        set(value) {
            wpiProperty.setValue(value)
        }


    override fun getValue(thisRef: Any?, property: KProperty<*>) = value

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
    }

    companion object {
        fun create(parent: Table, name: String, value: String) = TableEntry(parent, name, value)
        fun create(parent: Table, name: String, value: Boolean) = TableEntry(parent, name, value)
        fun <T : Number> create(parent: Table, name: String, value: T) = TableEntry(parent, name, value)
    }
}

fun groundControlToMajorTom() = Unit
