package org.sert2521.sertain.hardware

import org.sert2521.sertain.units.*
import kotlin.math.PI

class EncoderTicks(ticksPerRevolution: Int) : MetricUnit<Angular>(Angular, (PI * 2) / ticksPerRevolution)
