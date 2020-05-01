package org.sert2521.sertain.telemetry

import org.sert2521.sertain.coroutines.RobotScope

open class Table(val name: String, val parent: Table? = null) {
    constructor(name: String, vararg location: String) :
            this(name, if (location.isNotEmpty()) Table(location.last(), *location.dropLast(1).toTypedArray()) else null)

    val location: List<String> = parent?.let { it.location + it.name } ?: emptyList()

    fun <T> entry(name: String, value: T) = TableEntry(name, value, this)
    fun <T> linkEntry(name: String, get: () -> T) = RobotScope.linkTableEntry(name, this, get = get)
}
