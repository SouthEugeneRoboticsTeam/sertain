package org.sert2521.sertain.events

import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CoroutineScope
import org.sert2521.sertain.subsystems.Subsystem
import kotlin.coroutines.CoroutineContext

abstract class TaskEvent : Event()

class Use<R>(
        val subsystems: Set<Subsystem>,
        val important: Boolean,
        val callerContext: CoroutineContext,
        val continuation: CancellableContinuation<R>,
        val action: suspend CoroutineScope.() -> R
) : TaskEvent()
