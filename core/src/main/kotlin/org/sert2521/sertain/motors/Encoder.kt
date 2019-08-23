package org.sert2521.sertain.motors

import org.sert2521.sertain.units.Angular
import org.sert2521.sertain.units.MetricUnit
import org.sert2521.sertain.units.Seconds
import org.sert2521.sertain.units.div
import kotlin.math.PI

class Encoder(ticksPerRevolution: Int) {
    val ticks = EncoderTicks(ticksPerRevolution)
    val ticksPerSecond = ticks / Seconds
}

class EncoderTicks(ticksPerRevolution: Int) : MetricUnit<Angular>(Angular, (PI * 2) / ticksPerRevolution)
