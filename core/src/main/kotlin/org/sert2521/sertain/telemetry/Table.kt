package org.sert2521.sertain.telemetry

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.sert2521.sertain.coroutines.RobotScope
import org.sert2521.sertain.coroutines.periodic

open class Table(val name: String, val parent: Table? = null) {
    constructor(name: String, vararg location: String) :
            this(name, if (location.isNotEmpty()) Table(location.last(), *location.dropLast(1).toTypedArray()) else null)

    val location: List<String> = parent?.let { it.location + it.name } ?: emptyList()

    fun add(name: String, value: String) = TableEntry.create(this, name, value)
    fun add(name: String, value: Boolean) = TableEntry.create(this, name, value)
    fun <T : Number> add(name: String, value: T) = TableEntry.create(this, name, value)
    fun <T> add(name: String, value: T) = TableEntry.create(this, name, value)

    fun link(name: String, get: () -> String): Pair<Job, TableEntry<String>> {
        val entry= TableEntry.create(this, name, get())
        return RobotScope.launch {
            periodic {
                entry.value = get()
            }
        } to entry
    }
}
