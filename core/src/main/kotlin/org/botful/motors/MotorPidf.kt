package org.botful.motors

data class MotorPidf(
    val kp: Double = 0.0,
    val ki: Double = 0.0,
    val kd: Double = 0.0,
    val kf: Double = 0.0,
    val integralZone: Int = 0,
    val allowedError: Int = 0,
    val maxIntegral: Double = 0.0,
    val maxOutput: Double = 1.0,
    val period: Int = 0
)

class MotorPidfConfig : _root_ide_package_.org.botful.control.PidfConfig() {
    var integralZone: Int? = null
    var allowedError: Int? = null
    var maxIntegral: Double? = null
    var maxOutput: Double? = null
    var period: Int? = null
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
