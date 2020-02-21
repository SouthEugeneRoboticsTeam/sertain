package org.sert2521.sertain.subsystems

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import org.sert2521.sertain.events.Use
import org.sert2521.sertain.events.fire
import kotlin.coroutines.coroutineContext

suspend fun <S1 : Subsystem, R> use(s1: Accessor<S1>, cancelConflicts: Boolean = true, name: String = "Anonymous Task", action: suspend CoroutineScope.(S1) -> R) =
        use(s1.access(), cancelConflicts = cancelConflicts, name = name) { action(s1.access()) }
suspend fun <S1 : Subsystem, S2 : Subsystem, R> use(s1: Accessor<S1>, s2: Accessor<S2>, cancelConflicts: Boolean = true, name: String = "Anonymous Task", action: suspend CoroutineScope.(S1, S2) -> R) =
        use(s1.access(), s2.access(), cancelConflicts = cancelConflicts, name = name) { action(s1.access(), s2.access()) }
suspend fun <S1 : Subsystem, S2 : Subsystem, S3 : Subsystem, R> use(s1: Accessor<S1>, s2: Accessor<S2>, s3: Accessor<S3>, cancelConflicts: Boolean = true, name: String = "Anonymous Task", action: suspend CoroutineScope.(S1, S2, S3) -> R) =
        use(s1.access(), s2.access(), s3.access(), cancelConflicts = cancelConflicts, name = name) { action(s1.access(), s2.access(), s3.access()) }
suspend fun <S1 : Subsystem, S2 : Subsystem, S3 : Subsystem, S4 : Subsystem, R> use(s1: Accessor<S1>, s2: Accessor<S2>, s3: Accessor<S3>, s4: Accessor<S4>, cancelConflicts: Boolean = true, name: String = "Anonymous Task", action: suspend CoroutineScope.(S1, S2, S3, S4) -> R) =
        use(s1.access(), s2.access(), s3.access(), s4.access(), cancelConflicts = cancelConflicts, name = name) { action(s1.access(), s2.access(), s3.access(), s4.access()) }
suspend fun <S1 : Subsystem, S2 : Subsystem, S3 : Subsystem, S4 : Subsystem, S5 : Subsystem, R> use(s1: Accessor<S1>, s2: Accessor<S2>, s3: Accessor<S3>, s4: Accessor<S4>, s5: Accessor<S5>, cancelConflicts: Boolean = true, name: String = "Anonymous Task", action: suspend CoroutineScope.(S1, S2, S3, S4, S5) -> R) =
        use(s1.access(), s2.access(), s3.access(), s4.access(), s5.access(), cancelConflicts = cancelConflicts, name = name) { action(s1.access(), s2.access(), s3.access(), s4.access(), s5.access()) }
suspend fun <S1 : Subsystem, S2 : Subsystem, S3 : Subsystem, S4 : Subsystem, S5 : Subsystem, S6 : Subsystem, R> use(s1: Accessor<S1>, s2: Accessor<S2>, s3: Accessor<S3>, s4: Accessor<S4>, s5: Accessor<S5>, s6: Accessor<S6>, cancelConflicts: Boolean = true, name: String = "Anonymous Task", action: suspend CoroutineScope.(S1, S2, S3, S4, S5, S6) -> R) =
        use(s1.access(), s2.access(), s3.access(), s4.access(), s5.access(), s6.access(), cancelConflicts = cancelConflicts, name = name) { action(s1.access(), s2.access(), s3.access(), s4.access(), s5.access(), s6.access()) }
suspend fun <S1 : Subsystem, S2 : Subsystem, S3 : Subsystem, S4 : Subsystem, S5 : Subsystem, S6 : Subsystem, S7 : Subsystem, R> use(s1: Accessor<S1>, s2: Accessor<S2>, s3: Accessor<S3>, s4: Accessor<S4>, s5: Accessor<S5>, s6: Accessor<S6>, s7: Accessor<S7>, cancelConflicts: Boolean = true, name: String = "Anonymous Task", action: suspend CoroutineScope.(S1, S2, S3, S4, S5, S6, S7) -> R) =
        use(s1.access(), s2.access(), s3.access(), s4.access(), s5.access(), s6.access(), s7.access(), cancelConflicts = cancelConflicts, name = name) { action(s1.access(), s2.access(), s3.access(), s4.access(), s5.access(), s6.access(), s7.access()) }
suspend fun <S1 : Subsystem, S2 : Subsystem, S3 : Subsystem, S4 : Subsystem, S5 : Subsystem, S6 : Subsystem, S7 : Subsystem, S8 : Subsystem, R> use(s1: Accessor<S1>, s2: Accessor<S2>, s3: Accessor<S3>, s4: Accessor<S4>, s5: Accessor<S5>, s6: Accessor<S6>, s7: Accessor<S7>, s8: Accessor<S8>, cancelConflicts: Boolean = true, name: String = "Anonymous Task", action: suspend CoroutineScope.(S1, S2, S3, S4, S5, S6, S7, S8) -> R) =
        use(s1.access(), s2.access(), s3.access(), s4.access(), s5.access(), s6.access(), s7.access(), s8.access(), cancelConflicts = cancelConflicts, name = name) { action(s1.access(), s2.access(), s3.access(), s4.access(), s5.access(), s6.access(), s7.access(), s8.access()) }

suspend fun reserve(vararg subsystems: Accessor<*>) =
        use(*subsystems.map { it.access() }.toTypedArray()) {}
suspend fun <R> reserve(vararg subsystems: Accessor<*>, action: suspend CoroutineScope.() -> R) =
    use(*subsystems.map { it.access() }.toTypedArray()) { action() }

suspend fun <R> use(
    vararg subsystems: Subsystem,
    cancelConflicts: Boolean = true,
    name: String = "ANONYMOUS_TASK",
    action: suspend CoroutineScope.() -> R
): R {
    val context = coroutineContext
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
