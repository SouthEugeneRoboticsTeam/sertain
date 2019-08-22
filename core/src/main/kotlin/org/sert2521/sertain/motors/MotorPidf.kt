package org.sert2521.sertain.motors

import org.sert2521.sertain.control.PidfConfigure

data class MotorPidf(
        val kp: Double = 0.0,
        val ki: Double = 0.0,
        val kd: Double = 0.0,
        val kf: Double = 0.0,
        val integralZone: Int = 0,
        val allowedError: Int = 0,
        val maxIntegral: Double = 0.0,
        val maxOutput: Double = 0.0,
        val period: Int = 0
)

class MotorPidfConfigure : PidfConfigure() {
    var integralZone: Int = 0
    var allowedError: Int = 0
    var maxIntegral: Double = 0.0
    var maxOutput: Double = 0.0
    var period: Int = 0
}

class MotorPidfCollection(val motor: MotorController<*>, vararg initialPidf: Pair<Int, MotorPidf>) {
    private val pidfMap = initialPidf.toMap(mutableMapOf())

    fun toMap() = pidfMap

    operator fun set(slot: Int, pidf: MotorPidf) {
        pidfMap[slot] = pidf
        motor.updatePidf(slot, pidf)
    }

    operator fun get(slot: Int) = pidfMap[slot]
}
