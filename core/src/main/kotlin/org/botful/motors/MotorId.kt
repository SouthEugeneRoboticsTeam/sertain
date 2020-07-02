package org.botful.motors

import com.ctre.phoenix.motorcontrol.can.BaseMotorController
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import com.ctre.phoenix.motorcontrol.can.VictorSPX

sealed class MotorId(val number: Int)

class TalonId(number: Int) : MotorId(number)
class VictorId(number: Int) : MotorId(number)

internal fun ctreMotorController(id: MotorId): BaseMotorController {
    return when (id) {
        is TalonId -> TalonSRX(id.number)
        is VictorId -> VictorSPX(id.number)
    }
}
