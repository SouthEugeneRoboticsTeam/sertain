package org.sert2521.sertain.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.sert2521.sertain.coroutines.periodic
import kotlin.coroutines.coroutineContext

suspend fun timer(period: Long = 20, delay: Long = 0, timeout: Long = 0, action: (Long) -> Unit) {
    val startTime = System.currentTimeMillis()
    var time: Long
    if (timeout > 0) {
        CoroutineScope(coroutineContext).launch {
            periodic(period, delay) {
                time = System.currentTimeMillis() - startTime
                if (time < timeout) action(time)
                else this@launch.cancel()
            }
        }
    } else {
        CoroutineScope(coroutineContext).launch {
            periodic(period, delay) {
                time = System.currentTimeMillis() - startTime
                action(time)
            }
        }
    }.join()
}
