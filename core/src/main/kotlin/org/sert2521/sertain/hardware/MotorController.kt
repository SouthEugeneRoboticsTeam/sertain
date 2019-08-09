package org.sert2521.sertain.hardware

import com.ctre.phoenix.motorcontrol.ControlMode as CtreControlMode
import com.ctre.phoenix.motorcontrol.can.BaseMotorController as CtreMotorController

abstract class MotorController(val name: String, val ctreMotorController: CtreMotorController) {
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

    fun setVelocity(output: Double) {
        set(ControlMode.VELOCITY, output)
    }
}
