package org.sertain.motors

import org.sertain.units.AngularVelocityUnit
import org.sertain.units.angularUnit
import org.sertain.units.div
import org.sertain.units.seconds
import kotlin.math.PI

class Encoder(ticksPerRevolution: Int) {
    val ticks = encoderTicks(ticksPerRevolution)
    val ticksPerSecond: AngularVelocityUnit = ticks / seconds
}

fun encoderTicks(ticksPerRevolution: Int) = angularUnit((PI * 2) / ticksPerRevolution, " ticks")
