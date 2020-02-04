package org.sert2521.sertain.motors

import org.sert2521.sertain.units.AngularUnit
import org.sert2521.sertain.units.AngularVelocityUnit
import org.sert2521.sertain.units.Seconds
import org.sert2521.sertain.units.div
import kotlin.math.PI

class Encoder(ticksPerRevolution: Int) {
    val ticks = EncoderTicks(ticksPerRevolution)
    val ticksPerSecond: AngularVelocityUnit<EncoderTicks, Seconds> = ticks / Seconds
}

class EncoderTicks(ticksPerRevolution: Int) : AngularUnit((PI * 2) / ticksPerRevolution, " ticks")
