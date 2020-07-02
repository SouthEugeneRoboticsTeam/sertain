package org.botful.telemetry

import org.sert2521.sertain.coroutines.RobotScope

open class Table(val name: String, val parent: Table? = null) {
    constructor(name: String, vararg location: String) :
            this(name, if (location.isNotEmpty()) Table(location.last(), *location.dropLast(1).toTypedArray()) else null)

    val location: List<String> = parent?.let { it.location + it.name } ?: emptyList()
    fun <T> entry(value: T) = TableEntryProperty(value, this)
    fun <T> linkEntry(name: String, get: () -> T) = RobotScope.linkTableEntry(name, *location.toTypedArray(), get = get)
}
