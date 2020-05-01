package org.sert2521.sertain.telemetry

open class Table(val name: String, val parent: Table? = null) {
    val location: List<String> = parent?.let { it.location + it.name } ?: emptyList()

    fun child(name: String) = Table(name, this)

    fun add(name: String, value: String) = TableEntry.StringTableEntry()
}
