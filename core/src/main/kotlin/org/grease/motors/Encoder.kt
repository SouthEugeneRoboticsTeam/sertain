package org.grease.motors

import org.grease.units.AngularVelocityUnit
import org.grease.units.angularUnit
import org.grease.units.div
import org.grease.units.seconds
import kotlin.math.PI

class Encoder(ticksPerRevolution: Int) {
    val ticks = encoderTicks(ticksPerRevolution)
    val ticksPerSecond: AngularVelocityUnit = ticks / seconds
}

fun encoderTicks(ticksPerRevolution: Int) = angularUnit((PI * 2) / ticksPerRevolution, " ticks")
