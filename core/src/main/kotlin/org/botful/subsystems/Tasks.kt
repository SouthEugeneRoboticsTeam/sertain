package org.botful.subsystems

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import org.botful.events.fire
import kotlin.coroutines.coroutineContext

/**
 * Runs a command using the specified [worker] parameters. Can take up to 8 [Key]s as parameters.
 *
 * @param s1 The first worker to use.
 * @param s2 The second worker to use.
 * @param s3 The third worker to use.
 * @param s4 The fourth worker to use.
 * @param s5 The fifth worker to use.
 * @param s6 The sixth worker to use.
 * @param s7 The seventh worker to use.
 * @param s8 The eighth worker to use.
 * @param cancelConflicts Specifies weather the command should cancel conflicting commands.
 * @param name The name of the command for purposes of debugging.
 * @param action The action to run inside of the command. The action passes the values of the workers that were
 * @return Returns a [Result] containing the value returned from the [action] parameter.
 *
 * @sample { suspend fun myCommand() = use(subsys1, subsys2) { s1, s2 ->
 *      // use values here
 * }
 */

suspend fun <S1 : Subsystem, R> use(s1: S1, cancelConflicts: Boolean = true, name: String = "Unnamed Task", action: suspend CoroutineScope.(Key<S1>) -> R) =
        reserve(s1, cancelConflicts = cancelConflicts, name = name) { action(Key()) }
suspend fun <S1 : Subsystem, S2 : Subsystem, R> use(s1: S1, s2: S1, cancelConflicts: Boolean = true, name: String = "Unnamed Task", action: suspend CoroutineScope.(Key<S1>, Key<S2>) -> R) =
        reserve(s1, s2, cancelConflicts = cancelConflicts, name = name) { action(Key(), Key()) }
suspend fun <S1 : Subsystem, S2 : Subsystem, S3 : Subsystem, R> use(s1: S1, s2: S2, s3: S3, cancelConflicts: Boolean = true, name: String = "Unnamed Task", action: suspend CoroutineScope.(Key<S1>, Key<S2>, Key<S3>) -> R) =
        reserve(s1, s2, s3, cancelConflicts = cancelConflicts, name = name) { action(Key(), Key(), Key()) }
suspend fun <S1 : Subsystem, S2 : Subsystem, S3 : Subsystem, S4 : Subsystem, R> use(s1: S1, s2: S2, s3: S3, s4: S4, cancelConflicts: Boolean = true, name: String = "Unnamed Task", action: suspend CoroutineScope.(Key<S1>, Key<S2>, Key<S3>, Key<S4>) -> R) =
        reserve(s1, s2, s3, s4, cancelConflicts = cancelConflicts, name = name) { action(Key(), Key(), Key(), Key()) }
suspend fun <S1 : Subsystem, S2 : Subsystem, S3 : Subsystem, S4 : Subsystem, S5 : Subsystem, R> use(s1: S1, s2: S2, s3: S3, s4: S4, s5: S5, cancelConflicts: Boolean = true, name: String = "Unnamed Task", action: suspend CoroutineScope.(Key<S1>, Key<S2>, Key<S3>, Key<S4>, Key<S5>) -> R) =
        reserve(s1, s2, s3, s4, s5, cancelConflicts = cancelConflicts, name = name) { action(Key(), Key(), Key(), Key(), Key()) }
suspend fun <S1 : Subsystem, S2 : Subsystem, S3 : Subsystem, S4 : Subsystem, S5 : Subsystem, S6 : Subsystem, R> use(s1: S1, s2: S2, s3: S3, s4: S4, s5: S5, s6: S6, cancelConflicts: Boolean = true, name: String = "Unnamed Task", action: suspend CoroutineScope.(Key<S1>, Key<S2>, Key<S3>, Key<S4>, Key<S5>, Key<S6>) -> R) =
        reserve(s1, s2, s3, s4, s5, s6, cancelConflicts = cancelConflicts, name = name) { action(Key(), Key(), Key(), Key(), Key(), Key()) }
suspend fun <S1 : Subsystem, S2 : Subsystem, S3 : Subsystem, S4 : Subsystem, S5 : Subsystem, S6 : Subsystem, S7 : Subsystem, R> use(s1: S1, s2: S2, s3: S3, s4: S4, s5: S5, s6: S6, s7: S7, cancelConflicts: Boolean = true, name: String = "Unnamed Task", action: suspend CoroutineScope.(Key<S1>, Key<S2>, Key<S3>, Key<S4>, Key<S5>, Key<S6>, Key<S7>) -> R) =
        reserve(s1, s2, s3, s4, s5, s6, s7, cancelConflicts = cancelConflicts, name = name) { action(Key(), Key(), Key(), Key(), Key(), Key(), Key()) }
suspend fun <S1 : Subsystem, S2 : Subsystem, S3 : Subsystem, S4 : Subsystem, S5 : Subsystem, S6 : Subsystem, S7 : Subsystem, S8 : Subsystem, R> use(s1: S1, s2: S2, s3: S3, s4: S4, s5: S5, s6: S6, s7: S7, s8: S8, cancelConflicts: Boolean = true, name: String = "Unnamed Task", action: suspend CoroutineScope.(Key<S1>, Key<S2>, Key<S3>, Key<S4>, Key<S5>, Key<S6>, Key<S7>, Key<S8>) -> R) =
        reserve(s1, s2, s3, s4, s5, s6, s7, s8, cancelConflicts = cancelConflicts, name = name) { action(Key(), Key(), Key(), Key(), Key(), Key(), Key(), Key()) }

suspend operator fun <S : Subsystem, R> S.invoke(cancelConflicts: Boolean = true, name: String = "Unnamed Task", action: suspend CoroutineScope.(Key<S>) -> R) =
        reserve(this, cancelConflicts = cancelConflicts, name = name) { action(Key()) }

suspend fun reserve(
        vararg subsystems: Subsystem,
        cancelConflicts: Boolean = true,
        name: String = "Unnamed Task"
) = reserve(*subsystems, cancelConflicts = cancelConflicts, name = name) {}

suspend fun <R> reserve(
        vararg subsystems: Subsystem,
        cancelConflicts: Boolean = true,
        name: String = "Unnamed Task",
        action: suspend CoroutineScope.() -> R
): Result<R> {
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
