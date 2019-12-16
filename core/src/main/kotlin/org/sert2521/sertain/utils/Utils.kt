package org.sert2521.sertain.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.sert2521.sertain.coroutines.periodic
import kotlin.coroutines.coroutineContext

suspend fun timer(period: Long, delay: Long, timeout: Long, action: (Long) -> Unit) {
    val startTime = System.currentTimeMillis()
    var time: Long
    CoroutineScope(coroutineContext).launch {
        periodic(period, delay) {
            time = System.currentTimeMillis() - startTime
            if (time < timeout) action(time)
            else this@launch.cancel()
        }
    }.join()
}
