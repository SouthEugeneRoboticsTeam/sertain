package org.sert2521.sertain.motors

abstract class TalonMotorController() : PhoenixMotorController() {
    abstract val current: Double
}
