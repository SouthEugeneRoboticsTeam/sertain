package org.grease.utils

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun <T> get(get: () -> T) = object : ReadOnlyProperty<Any?, T> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return get()
    }
}
