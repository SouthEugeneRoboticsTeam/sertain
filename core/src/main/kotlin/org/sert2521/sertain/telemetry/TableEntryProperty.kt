package org.sert2521.sertain.telemetry

import org.sert2521.sertain.coroutines.RobotScope
import org.sert2521.sertain.coroutines.watch
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class TableEntryProperty<T>(private val initialValue: T, val location: List<String> = emptyList()) : ReadWriteProperty<Any?, T> {
    var entry: TableEntry<T>? = null
        private set

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        if (entry == null) {
            entry = TableEntry(property.name, initialValue, location)
        }
        return entry?.value ?: initialValue
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        entry?.value = value
    }
}

fun <T> tableEntry(value: T, vararg location: String) = TableEntryProperty(value, location.toList())

fun <T> RobotScope.linkTableEntry(name: String, location: List<String> = emptyList(), get: () -> T) = run {
    val entry = TableEntry(name, get(), location)
    get.watch {
        onChange {
            entry.value = value
        }
    }
}

fun <T> RobotScope.withTableEntry(name: String, value: T, vararg location: String, action: (T) -> Unit) =
        withTableEntry(TableEntry(name, value, location.toList()), action)

fun <T> RobotScope.withTableEntry(entry: TableEntry<T>, action: (T) -> Unit) = run {
    action(entry.value)
    ({ entry.value }).watch {
        onChange {
            action(value)
        }
    }
}
