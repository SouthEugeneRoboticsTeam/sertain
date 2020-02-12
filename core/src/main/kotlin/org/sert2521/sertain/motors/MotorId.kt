package org.sert2521.sertain.motors

sealed class MotorId(val number: Int)

class TalonId(number: Int) : MotorId(number)
class VictorId(number: Int) : MotorId(number)
