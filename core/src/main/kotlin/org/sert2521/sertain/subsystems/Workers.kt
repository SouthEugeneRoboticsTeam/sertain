package org.sert2521.sertain.subsystems

object Workers {
    private val workers = mutableListOf<Worker<*>>()

    fun add(worker: Worker<*>) {
        workers += worker
    }

    internal suspend fun setupAll() {
        workers.forEach { it.setupIfNotNull() }
    }

    internal suspend fun defaultAll() {
        workers.forEach { it.defaultIfNotNull() }
    }
}
