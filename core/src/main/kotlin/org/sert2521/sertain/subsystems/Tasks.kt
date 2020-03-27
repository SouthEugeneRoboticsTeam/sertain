package org.sert2521.sertain.subsystems

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import org.sert2521.sertain.events.Use
import org.sert2521.sertain.events.fire
import kotlin.coroutines.coroutineContext

suspend fun <S1, R> use(s1: Subsystem<S1>, cancelConflicts: Boolean = true, name: String = "Anonymous Task", action: suspend CoroutineScope.(S1) -> R) =
        reserve(s1, cancelConflicts = cancelConflicts, name = name) { action(s1.value) }
suspend fun <S1, S2, R> use(s1: Subsystem<S1>, s2: Subsystem<S2>, cancelConflicts: Boolean = true, name: String = "Anonymous Task", action: suspend CoroutineScope.(S1, S2) -> R) =
        reserve(s1, s2, cancelConflicts = cancelConflicts, name = name) { action(s1.value, s2.value) }
suspend fun <S1, S2, S3, R> use(s1: Subsystem<S1>, s2: Subsystem<S2>, s3: Subsystem<S3>, cancelConflicts: Boolean = true, name: String = "Anonymous Task", action: suspend CoroutineScope.(S1, S2, S3) -> R) =
        reserve(s1, s2, s3, cancelConflicts = cancelConflicts, name = name) { action(s1.value, s2.value, s3.value) }
suspend fun <S1, S2, S3, S4, R> use(s1: Subsystem<S1>, s2: Subsystem<S2>, s3: Subsystem<S3>, s4: Subsystem<S4>, cancelConflicts: Boolean = true, name: String = "Anonymous Task", action: suspend CoroutineScope.(S1, S2, S3, S4) -> R) =
        reserve(s1, s2, s3, s4, cancelConflicts = cancelConflicts, name = name) { action(s1.value, s2.value, s3.value, s4.value) }
suspend fun <S1, S2, S3, S4, S5, R> use(s1: Subsystem<S1>, s2: Subsystem<S2>, s3: Subsystem<S3>, s4: Subsystem<S4>, s5: Subsystem<S5>, cancelConflicts: Boolean = true, name: String = "Anonymous Task", action: suspend CoroutineScope.(S1, S2, S3, S4, S5) -> R) =
        reserve(s1, s2, s3, s4, s5, cancelConflicts = cancelConflicts, name = name) { action(s1.value, s2.value, s3.value, s4.value, s5.value) }
suspend fun <S1, S2, S3, S4, S5, S6, R> use(s1: Subsystem<S1>, s2: Subsystem<S2>, s3: Subsystem<S3>, s4: Subsystem<S4>, s5: Subsystem<S5>, s6: Subsystem<S6>, cancelConflicts: Boolean = true, name: String = "Anonymous Task", action: suspend CoroutineScope.(S1, S2, S3, S4, S5, S6) -> R) =
        reserve(s1, s2, s3, s4, s5, s6, cancelConflicts = cancelConflicts, name = name) { action(s1.value, s2.value, s3.value, s4.value, s5.value, s6.value) }
suspend fun <S1, S2, S3, S4, S5, S6, S7, R> use(s1: Subsystem<S1>, s2: Subsystem<S2>, s3: Subsystem<S3>, s4: Subsystem<S4>, s5: Subsystem<S5>, s6: Subsystem<S6>, s7: Subsystem<S7>, cancelConflicts: Boolean = true, name: String = "Anonymous Task", action: suspend CoroutineScope.(S1, S2, S3, S4, S5, S6, S7) -> R) =
        reserve(s1, s2, s3, s4, s5, s6, s7, cancelConflicts = cancelConflicts, name = name) { action(s1.value, s2.value, s3.value, s4.value, s5.value, s6.value, s7.value) }
suspend fun <S1, S2, S3, S4, S5, S6, S7, S8, R> use(s1: Subsystem<S1>, s2: Subsystem<S2>, s3: Subsystem<S3>, s4: Subsystem<S4>, s5: Subsystem<S5>, s6: Subsystem<S6>, s7: Subsystem<S7>, s8: Subsystem<S8>, cancelConflicts: Boolean = true, name: String = "Anonymous Task", action: suspend CoroutineScope.(S1, S2, S3, S4, S5, S6, S7, S8) -> R) =
        reserve(s1, s2, s3, s4, s5, s6, s7, s8, cancelConflicts = cancelConflicts, name = name) { action(s1.value, s2.value, s3.value, s4.value, s5.value, s6.value, s7.value, s8.value) }

suspend fun reserve(
        vararg subsystems: Subsystem<*>,
        cancelConflicts: Boolean = true,
        name: String = "ANONYMOUS_TASK"
) = reserve(*subsystems, cancelConflicts = cancelConflicts, name = name) {}

suspend fun <R> reserve(
        vararg subsystems: Subsystem<*>,
        cancelConflicts: Boolean = true,
        name: String = "ANONYMOUS_TASK",
        action: suspend CoroutineScope.() -> R
): Result<R> {
    val context = coroutineContext
    println("Calling reserve!")
    return suspendCancellableCoroutine { continuation ->
        CoroutineScope(context).launch {
            fire(Use(
                    subsystems.toSet(),
                    cancelConflicts,
                    name,
                    context,
                    continuation,
                    action
            ))
        }
    }
}
