package org.sert2521.sertain.networktables

open class Table(val name: String, val location: List<String> = emptyList()) {
    fun <T> entry(value: T) = TableEntry(value, location + name)
}
