package org.sert2521.sertain.subsystems

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import org.sert2521.sertain.events.Use
import org.sert2521.sertain.events.fire
import kotlin.coroutines.coroutineContext

/**
 * Runs a command using the specified [worker] parameters. Can take up to 8 [Worker]s as parameters.
 *
 * @param w1 The first worker to use.
 * @param w2 The second worker to use.
 * @param w3 The third worker to use.
 * @param w4 The fourth worker to use.
 * @param w5 The fifth worker to use.
 * @param w6 The sixth worker to use.
 * @param w7 The seventh worker to use.
 * @param w8 The eighth worker to use.
 * @param cancelConflicts Specifies weather the command should cancel conflicting commands.
 * @param name The name of the command for purposes of debugging.
 * @param action The action to run inside of the command. The action passes the values of the workers that were
 * @return Returns a [Result] containing the value returned from the [action] parameter.
 *
 * @sample { suspend fun myCommand() = use(subsys1, subsys2) { s1, s2 ->
 *      // use values here
 * }
 * }
 */
suspend fun <S1, R> use(w1: Worker<S1>, cancelConflicts: Boolean = true, name: String = "Unnamed Task", action: suspend CoroutineScope.(S1) -> R) =
        reserve(w1, cancelConflicts = cancelConflicts, name = name) { action(w1.value) }
suspend fun <S1, S2, R> use(w1: Worker<S1>, w2: Worker<S2>, cancelConflicts: Boolean = true, name: String = "Unnamed Task", action: suspend CoroutineScope.(S1, S2) -> R) =
        reserve(w1, w2, cancelConflicts = cancelConflicts, name = name) { action(w1.value, w2.value) }
suspend fun <S1, S2, S3, R> use(w1: Worker<S1>, w2: Worker<S2>, w3: Worker<S3>, cancelConflicts: Boolean = true, name: String = "Unnamed Task", action: suspend CoroutineScope.(S1, S2, S3) -> R) =
        reserve(w1, w2, w3, cancelConflicts = cancelConflicts, name = name) { action(w1.value, w2.value, w3.value) }
suspend fun <S1, S2, S3, S4, R> use(w1: Worker<S1>, w2: Worker<S2>, w3: Worker<S3>, w4: Worker<S4>, cancelConflicts: Boolean = true, name: String = "Unnamed Task", action: suspend CoroutineScope.(S1, S2, S3, S4) -> R) =
        reserve(w1, w2, w3, w4, cancelConflicts = cancelConflicts, name = name) { action(w1.value, w2.value, w3.value, w4.value) }
suspend fun <S1, S2, S3, S4, S5, R> use(w1: Worker<S1>, w2: Worker<S2>, w3: Worker<S3>, w4: Worker<S4>, w5: Worker<S5>, cancelConflicts: Boolean = true, name: String = "Unnamed Task", action: suspend CoroutineScope.(S1, S2, S3, S4, S5) -> R) =
        reserve(w1, w2, w3, w4, w5, cancelConflicts = cancelConflicts, name = name) { action(w1.value, w2.value, w3.value, w4.value, w5.value) }
suspend fun <S1, S2, S3, S4, S5, S6, R> use(w1: Worker<S1>, w2: Worker<S2>, w3: Worker<S3>, w4: Worker<S4>, w5: Worker<S5>, w6: Worker<S6>, cancelConflicts: Boolean = true, name: String = "Unnamed Task", action: suspend CoroutineScope.(S1, S2, S3, S4, S5, S6) -> R) =
        reserve(w1, w2, w3, w4, w5, w6, cancelConflicts = cancelConflicts, name = name) { action(w1.value, w2.value, w3.value, w4.value, w5.value, w6.value) }
suspend fun <S1, S2, S3, S4, S5, S6, S7, R> use(w1: Worker<S1>, w2: Worker<S2>, w3: Worker<S3>, w4: Worker<S4>, w5: Worker<S5>, w6: Worker<S6>, w7: Worker<S7>, cancelConflicts: Boolean = true, name: String = "Unnamed Task", action: suspend CoroutineScope.(S1, S2, S3, S4, S5, S6, S7) -> R) =
        reserve(w1, w2, w3, w4, w5, w6, w7, cancelConflicts = cancelConflicts, name = name) { action(w1.value, w2.value, w3.value, w4.value, w5.value, w6.value, w7.value) }
suspend fun <S1, S2, S3, S4, S5, S6, S7, S8, R> use(w1: Worker<S1>, w2: Worker<S2>, w3: Worker<S3>, w4: Worker<S4>, w5: Worker<S5>, w6: Worker<S6>, w7: Worker<S7>, w8: Worker<S8>, cancelConflicts: Boolean = true, name: String = "Unnamed Task", action: suspend CoroutineScope.(S1, S2, S3, S4, S5, S6, S7, S8) -> R) =
        reserve(w1, w2, w3, w4, w5, w6, w7, w8, cancelConflicts = cancelConflicts, name = name) { action(w1.value, w2.value, w3.value, w4.value, w5.value, w6.value, w7.value, w8.value) }

suspend fun reserve(
        vararg workers: Worker<*>,
        cancelConflicts: Boolean = true,
        name: String = "Unnamed Task"
) = reserve(*workers, cancelConflicts = cancelConflicts, name = name) {}

suspend fun <R> reserve(
        vararg workers: Worker<*>,
        cancelConflicts: Boolean = true,
        name: String = "Unnamed Task",
        action: suspend CoroutineScope.() -> R
): Result<R> {
    val context = coroutineContext
    return suspendCancellableCoroutine { continuation ->
        CoroutineScope(context).launch {
            fire(Use(
                    workers.toSet(),
                    cancelConflicts,
                    name,
                    context,
                    continuation,
                    action
            ))
        }
    }
}
