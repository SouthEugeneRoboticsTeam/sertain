package org.sert2521.sertain.subsystems

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

internal suspend fun <T> Worker<T>.defaultIfNotNull() {
    if (default != null) {
        CoroutineScope(coroutineContext).launch {
            reserve(this@defaultIfNotNull, cancelConflicts = false) {
                default?.invoke(this, value)
            }
        }
    }
}

internal suspend fun <T> Worker<T>.setupIfNotNull() {
    if (setup != null) {
        CoroutineScope(coroutineContext).launch {
            setup?.invoke(this, value)
        }
    }
}
