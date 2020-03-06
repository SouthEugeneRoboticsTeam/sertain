package org.sert2521.sertain.telemetry

import kotlinx.coroutines.CoroutineScope
import org.sert2521.sertain.coroutines.watch
import org.sert2521.sertain.events.onTick

fun <T> CoroutineScope.linkTableEntry(name: String, parent: Table, get: () -> T) = run {
    val entry = TableEntry(name, get(), parent)
    onTick {
        entry.value = get()
    }
}

fun <T> CoroutineScope.linkTableEntry(name: String, vararg location: String, get: () -> T) = run {
    val entry = TableEntry(name, get(), *location)
    onTick {
        entry.value = get()
    }
}

fun <T> CoroutineScope.withTableEntry(name: String, value: T, vararg location: String, action: (T) -> Unit) =
        withTableEntry(TableEntry(name, value, *location), action)

fun <T> CoroutineScope.withTableEntry(name: String, value: T, parent: Table, action: (T) -> Unit) =
        withTableEntry(TableEntry(name, value, parent), action)

fun <T> CoroutineScope.withTableEntry(entry: TableEntry<T>, action: (T) -> Unit) = run {
    action(entry.value)
    ({ entry.value }).watch {
        onChange {
            action(value)
        }
    }
}
