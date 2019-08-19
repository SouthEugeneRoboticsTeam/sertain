package org.sert2521.sertain.hardware

import org.sert2521.sertain.control.PidfConfigure

class MotorPidfConfigure : PidfConfigure() {
    val integralZone: Int = 0
    val allowedError: Int = 0
    val maxIntegral: Double = 0.0
    val maxOutput: Double = 0.0
    val period: Int = 0
}
