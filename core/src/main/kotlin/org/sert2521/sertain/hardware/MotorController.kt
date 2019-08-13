package org.sert2521.sertain.hardware

import org.sert2521.sertain.units.*
import java.text.FieldPosition
import com.ctre.phoenix.motorcontrol.ControlMode as CtreControlMode
import com.ctre.phoenix.motorcontrol.can.BaseMotorController as CtreMotorController

class MotorController(val name: String, val ctreMotorController: CtreMotorController) {
    val encoderTicks = EncoderTicks(4096)


    enum class ControlMode {
        PERCENT_OUTPUT,
        POSITION,
        VELOCITY
    }

    fun set(controlMode: ControlMode, output: Double) {
        ctreMotorController.set(
                when (controlMode) {
                    ControlMode.PERCENT_OUTPUT -> CtreControlMode.PercentOutput
                    ControlMode.POSITION -> CtreControlMode.Position
                    ControlMode.VELOCITY -> CtreControlMode.Velocity
                },
                output
        )
    }

    fun setPercentOutput(output: Double) {
        set(ControlMode.PERCENT_OUTPUT, output)
    }

    fun setPosition(output: Double) {
        set(ControlMode.POSITION, output)
    }

    fun setVelocity(output: Double) {
        set(ControlMode.VELOCITY, output)
    }

    fun <U : MetricUnit<Angular>> set(position: MetricValue<Angular, U>) {
        setPosition(position.convertTo(encoderTicks).value)
    }
}
