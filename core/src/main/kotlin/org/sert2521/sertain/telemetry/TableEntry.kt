package org.sert2521.sertain.telemetry

import edu.wpi.first.networktables.NetworkTable
import edu.wpi.first.networktables.NetworkTableInstance
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

private fun wpiTable(location: List<String>): NetworkTable {
    var table = NetworkTableInstance.getDefault().getTable(location.first())
    location.drop(1).dropLast(0).forEach {
        table = table.getSubTable(it)
    }
    return table
}

sealed class TableEntry<T> private constructor(val parent: Table, val name: String, value: T) : ReadWriteProperty<Any?, T> {
    val location = parent.location + parent.name

    private val wpiParent = wpiTable(location)
    private val wpiEntry = wpiParent.getEntry(name)

    init {
        wpiEntry.setValue(this.wrap(value))
    }

    var value: T
        get() = unwrap(wpiEntry.value.value)
        set(value) {
            wpiEntry.setValue(wrap(value))
        }

    protected open fun wrap(value: T): Any? = value
    protected open fun unwrap(value: Any?): T = value as T

    override fun getValue(thisRef: Any?, property: KProperty<*>): T = value
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
    }

    class StringTableEntry(parent: Table, name: String, value: String) : TableEntry<String>(parent, name, value)
    class NumberTableEntry<T : Number>(parent: Table, name: String, value: T) : TableEntry<T>(parent, name, value)
    class BooleanTableEntry(parent: Table, name: String, value: Boolean) : TableEntry<Boolean>(parent, name, value)
}

fun groundControlToMajorTom() = Unit
