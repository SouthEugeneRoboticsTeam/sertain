package org.sert2521.sertain.telemetry

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.sert2521.sertain.coroutines.periodic
import org.sert2521.sertain.coroutines.watch

fun CoroutineScope.linkTableEntry(name: String, parent: Table, get: () -> Any?) {
    val entry = TableEntry.create(parent, name, get())
    launch {
        periodic {
            entry.value = get()
        }
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
