package org.sert2521.sertain.telemetry

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class TableEntryProperty<T>(private val initialValue: T, parent: Table? = null, val name: String? = null) : ReadWriteProperty<Any?, T> {
    constructor(initialValue: T, vararg location: String, name: String?) :
            this(initialValue, Table(location.last(), *location.dropLast(1).toTypedArray()), name)

    val location = parent?.let { it.location + it.name } ?: emptyList()

    var entry: TableEntry<T>? = null
        private set

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        if (entry == null) {
            entry = TableEntry(name ?: property.name, initialValue, *location.toTypedArray())
        }
        return entry?.value ?: initialValue
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        entry?.value = value
    }
}

fun <T> tableEntry(value: T, parent: Table?, name: String? = null) = TableEntryProperty(value, parent, name = name)
fun <T> tableEntry(value: T, vararg location: String, name: String? = null) = TableEntryProperty(value, *location, name = name)
