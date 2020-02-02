package org.sert2521.sertain.motors

import org.sert2521.sertain.units.Angular
import org.sert2521.sertain.units.AngularVelocity
import org.sert2521.sertain.units.MetricUnit
import org.sert2521.sertain.units.seconds
import org.sert2521.sertain.units.div
import kotlin.math.PI

class Encoder(ticksPerRevolution: Int) {
    val ticks = EncoderTicks(ticksPerRevolution)
    val ticksPerSecond: MetricUnit<AngularVelocity> = ticks / seconds
}

class EncoderTicks(ticksPerRevolution: Int) : MetricUnit<Angular>(Angular, (PI * 2) / ticksPerRevolution, " ticks")
