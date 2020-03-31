package org.sert2521.sertain.events

import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import org.sert2521.sertain.subsystems.Worker
import kotlin.coroutines.CoroutineContext

interface SubsystemEvent : Event

class Use<R>(
        val workers: Set<Worker<*>>,
        val cancelConflicts: Boolean,
        val name: String,
        val context: CoroutineContext,
        val continuation: CancellableContinuation<Result<R>>,
        val action: suspend CoroutineScope.() -> R
) : SubsystemEvent

class Clean(
        val workers: Set<Worker<*>>,
        val job: Job
) : SubsystemEvent
