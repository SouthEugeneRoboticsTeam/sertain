package org.sert2521.sertain.telemetry

import org.sert2521.sertain.coroutines.RobotScope

open class Table(val name: String, val location: List<String> = emptyList()) {
    fun <T> entry(value: T) = TableEntryProperty(value, location + name)
    fun <T> linkEntry(name: String, get: () -> T) = RobotScope.linkTableEntry(name, location, get)
}
