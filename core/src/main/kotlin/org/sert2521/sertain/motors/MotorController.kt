package org.sert2521.sertain.motors

import org.sert2521.sertain.units.*
import com.ctre.phoenix.motorcontrol.NeutralMode as CtreNeutralMode

abstract class MotorController {
    abstract fun eachMotor(configure: MotorController.() -> Unit)

    abstract fun eachFollower(configure: MotorController.() -> Unit)

    abstract var inverted: Boolean

    open var sensorInverted: Boolean = false

    open var openLoopRamp: Double = 0.0

    open var closedLoopRamp: Double = 0.0

    open var minOutputRange: ClosedRange<Double> = 0.0..0.0

    open var maxOutputRange: ClosedRange<Double> = -1.0..1.0

    abstract val percentOutput: Double

    abstract fun setPercentOutput(output: Double)

    abstract fun <U : AngularUnit> position(unit: U): MetricValue<Angular, U>

    abstract fun <U1 : AngularVelocity, U2 : ChronicUnit, U : AngularVelocityUnit<U1, U2>> velocity(unit: U): MetricValue<CompositeUnitType<Per, Angular, Chronic>, U>

    abstract fun <U : AngularUnit, V : AngularValue<U>> setTargetPosition(position: V)

    abstract fun <U1 : AngularUnit, U2 : ChronicUnit, V : AngularVelocityValue<U1, U2>> setTargetVelocity(velocity: V)

    abstract fun disable()

    internal abstract fun updatePidf(slot: Int, pidf: MotorPidf)
}

fun<T: MotorId> motorController(id: T, vararg followerIds: MotorId, configure: MotorController.() -> Unit = {}): MotorController {
    return when (id) {
        is TalonId  -> TalonMotorController(id,  *followerIds, configure = configure)
        is VictorId -> VictorMotorController(id, *followerIds, configure = configure)
        else -> throw NotImplementedError("A Motor Controller is not implemented for this ID")
    }
}

enum class ControlMode {
    PERCENT_OUTPUT,
    POSITION,
    VELOCITY,
    CURRENT,
    DISABLED
}

fun ctreNeutralMode(brakeMode: Boolean): CtreNeutralMode =
        if (brakeMode) CtreNeutralMode.Brake else CtreNeutralMode.Coast
