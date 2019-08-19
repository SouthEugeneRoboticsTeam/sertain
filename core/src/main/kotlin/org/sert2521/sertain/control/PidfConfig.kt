package org.sert2521.sertain.control

data class PidfSettings(
        val kp: Double = 0.0,
        val ki: Double = 0.0,
        val kd: Double = 0.0,
        val kf: Double = 0.0
)

open class PidfConfigure {
    var kp = 0.0
    var ki = 0.0
    var kd = 0.0
    var kf = 0.0
}
