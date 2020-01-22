package org.sert2521.sertain.subsystems

import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

internal var subsystems = mutableMapOf<KClass<*>, Subsystem>()

fun <S : Subsystem> add(reference: KClass<*>, subsystem: S) {
    subsystems[reference] = subsystem
}

fun <S : Subsystem> add(subsystem: S) {
    add(subsystem::class, subsystem)
}

inline fun <reified S : Subsystem> add() {
    add(S::class, S::class.createInstance())
}

fun <S : Subsystem> access(reference: KClass<S>): S {
    @Suppress("unchecked_cast") // Safe because subsystems is internally managed
    subsystems[reference]?.let {
        return it as S
    }
    throw IllegalStateException("Subsystem with type ${reference.qualifiedName} does not exist." +
            " Did you forget to add it?")
}

inline fun <reified S : Subsystem> access() = access(S::class)

inline fun <reified S : Subsystem> TaskConfigure<*>.use(): S {
    val subsystem = access(S::class)
    this += subsystem
    return subsystem
}
