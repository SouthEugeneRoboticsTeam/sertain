package org.sertain.control

import kotlin.math.sign

open class PidfConfig {
    var kp: Double? = null
    var ki: Double? = null
    var kd: Double? = null
    var kf: Double? = null
}

class PidfController(config: PidfConfig, val dt: Double) {
    private val kp = config.kp ?: 0.0
    private val ki = config.ki ?: 0.0
    private val kd = config.kd ?: 0.0
    private val kf = config.kf ?: 0.0

    private var integral = 0.0
    private var lastError = 0.0

    fun next(setPoint: Double, actual: Double): Double {
        val error = setPoint - actual
        integral += error * dt
        val derivative = (error - lastError) / dt
        lastError = error
        return (kp * error) + (ki * integral) + (kd * derivative) + (kf * sign(error))
    }
}
