package org.sert2521.sertain.hardware

import org.sert2521.sertain.units.*
import com.ctre.phoenix.motorcontrol.ControlMode as CtreControlMode
import com.ctre.phoenix.motorcontrol.can.BaseMotorController as CtreMotorController

class MotorController(val name: String, val ctreMotorController: CtreMotorController) {
    val encoderTicks = EncoderTicks(4096)

    fun setPercent(output: Double) {
        ctreMotorController.set(CtreControlMode.PercentOutput, output)
    }

    fun setPosition(output: Double) {
        ctreMotorController.set(CtreControlMode.Position, output)
    }

    fun setVelocity(output: Double) {
        ctreMotorController.set(CtreControlMode.Velocity, output)
    }

    fun <U : MetricUnit<Angular>> set(position: MetricValue<Angular, U>) {
        setPosition(position.convertTo(encoderTicks).value)
    }
}
