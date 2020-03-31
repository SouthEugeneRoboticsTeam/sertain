package org.sert2521.sertain.subsystems

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.security.InvalidParameterException
import kotlin.coroutines.coroutineContext

object Workers {
    private val workers = mutableSetOf<Worker<*>>()

    fun add(worker: Worker<*>) {
        workers += worker
    }

    internal suspend fun <T> defaultOf(worker: Worker<T>) {
        if (!workers.contains(worker))
            throw InvalidParameterException("The worker's default function can only be run if it is first added.")
        if (worker.default != null) {
            CoroutineScope(coroutineContext).launch {
                reserve(worker, cancelConflicts = false) {
                    worker.default?.invoke(this, worker.value)
                }
            }
        }
    }

    internal suspend fun <T> setupOf(worker: Worker<T>) {
        if (!workers.contains(worker))
            throw InvalidParameterException("The worker's setup function can only be run if it is first added.")
        if (worker.setup != null) {
            CoroutineScope(coroutineContext).launch {
                worker.setup?.invoke(this, worker.value)
            }
        }
    }


    internal suspend fun setupAll() = workers.forEach { setupOf(it) }

    internal suspend fun defaultAll() = workers.forEach { defaultOf(it) }
}
