package org.sert2521.sertain.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import org.sert2521.sertain.units.Chronic
import org.sert2521.sertain.units.MetricValue
import org.sert2521.sertain.units.ms
import kotlin.coroutines.coroutineContext

suspend inline fun periodic(
        period: MetricValue<Chronic> = 20.ms,
        delay: MetricValue<Chronic> = 0.ms,
        time: MetricValue<Chronic> = 0.ms,
        crossinline action: () -> Unit
) {
    val job = CoroutineScope(coroutineContext).launch {
        delay(delay)
        while (true) {
            action()
            delay(period)
        }
    }
    if (time.value > 0) {
        delay(time)
        job.cancelAndJoin()
    }
}
