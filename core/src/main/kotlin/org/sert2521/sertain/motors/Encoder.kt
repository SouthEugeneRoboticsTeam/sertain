package org.sert2521.sertain.motors

import org.sert2521.sertain.units.AngularVelocity
import org.sert2521.sertain.units.AngularVelocityUnit
import org.sert2521.sertain.units.MetricUnit
import org.sert2521.sertain.units.angularUnit
import org.sert2521.sertain.units.div
import org.sert2521.sertain.units.seconds
import kotlin.math.PI

class Encoder(ticksPerRevolution: Int) {
    val ticks = encoderTicks(ticksPerRevolution)
    val ticksPerSecond: AngularVelocityUnit = ticks / seconds
}

fun encoderTicks(ticksPerRevolution: Int) = angularUnit((PI * 2) / ticksPerRevolution, " ticks")
