package org.sert2521.sertain.control

open class PidfConfig {
    var kp = 0.0
    var ki = 0.0
    var kd = 0.0
    var kf = 0.0
}

class Pidf(config: PidfConfig, val dt: Double) {
    private val kp = config.kp
    private val ki = config.ki
    private val kd = config.kd
    private val kf = config.kf

    private var integral = 0.0
    private var lastError = 0.0

    fun next(setPoint: Double, actual: Double): Double {
        val error = setPoint - actual
        integral += error * dt
        val derivative = (error - lastError) / dt
        lastError = error
        return (kp * error) + (ki * integral) + (kd * derivative) + kf
    }
}
