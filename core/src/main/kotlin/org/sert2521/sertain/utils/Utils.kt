package org.sert2521.sertain.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.sert2521.sertain.coroutines.periodic
import org.sert2521.sertain.units.Chronic
import org.sert2521.sertain.units.MetricValue
import org.sert2521.sertain.units.from
import org.sert2521.sertain.units.milliseconds
import org.sert2521.sertain.units.ms
import kotlin.coroutines.coroutineContext

suspend fun timer(period: MetricValue<Chronic> = 20.ms, delay: MetricValue<Chronic> = 0.ms, timeout: MetricValue<Chronic> = 0.ms, action: (Long) -> Unit) {
    val startTime = System.currentTimeMillis()
    var time: Long
    if (timeout.value > 0) {
        CoroutineScope(coroutineContext).launch {
            periodic(period, delay) {
                time = System.currentTimeMillis() - startTime
                if (time < timeout.from(milliseconds)) action(time)
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
