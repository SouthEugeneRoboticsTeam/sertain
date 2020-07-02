package org.botful.motors

import org.botful.units.AngularVelocityUnit
import org.botful.units.angularUnit
import org.botful.units.div
import org.botful.units.seconds
import kotlin.math.PI

class Encoder(ticksPerRevolution: Int) {
    val ticks = encoderTicks(ticksPerRevolution)
    val ticksPerSecond: AngularVelocityUnit = ticks / seconds
}

fun encoderTicks(ticksPerRevolution: Int) = angularUnit((PI * 2) / ticksPerRevolution, " ticks")
