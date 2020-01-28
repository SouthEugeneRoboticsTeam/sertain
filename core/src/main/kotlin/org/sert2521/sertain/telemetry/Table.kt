package org.sert2521.sertain.telemetry

import org.sert2521.sertain.coroutines.RobotScope

open class Table(val name: String, vararg location: String) {
    val location: List<String> = location.toList()
    fun <T> entry(value: T) = TableEntryProperty(value, *location.toTypedArray() + name)
    fun <T> linkEntry(name: String, get: () -> T) = RobotScope.linkTableEntry(name, *location.toTypedArray(), get = get)
}
