package org.sert2521.sertain.telemetry

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.sert2521.sertain.coroutines.periodic
import org.sert2521.sertain.observables.onChange
import org.sert2521.sertain.observables.watch

fun <T> CoroutineScope.linkTableEntry(name: String, parent: Table, get: () -> T) = run {
    val entry = TableEntry(name, get(), parent)
    launch {
        periodic {
            entry.value = get()
        }
    }
}

fun <T> CoroutineScope.linkTableEntry(name: String, vararg location: String, get: () -> T) = run {
    val entry = TableEntry(name, get(), *location)
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
    watch { entry.value }.onChange { action(it.value) }
}
