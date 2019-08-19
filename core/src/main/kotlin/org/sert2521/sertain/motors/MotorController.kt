package org.sert2521.sertain.motors

import com.ctre.phoenix.motorcontrol.FeedbackDevice
import org.sert2521.sertain.control.PidfConfigure
import org.sert2521.sertain.units.*
import com.ctre.phoenix.motorcontrol.can.TalonSRX as CtreTalon
import com.ctre.phoenix.motorcontrol.can.VictorSPX as CtreVictor
import com.ctre.phoenix.motorcontrol.ControlMode as CtreControlMode
import com.ctre.phoenix.motorcontrol.NeutralMode as CtreNeutralMode
import com.ctre.phoenix.motorcontrol.can.BaseMotorController as CtreMotorController

sealed class MotorId(val number: Int)

class TalonId(number: Int) : MotorId(number)
class VictorId(number: Int) : MotorId(number)

fun ctreMotorController(id: MotorId): CtreMotorController {
    return when (id) {
        is TalonId -> CtreTalon(id.number)
        is VictorId -> CtreVictor(id.number)
    }
}

open class MotorController<T : MotorId>(val id: T, vararg followerIds: MotorId, val name: String = "ANONYMOUS_MOTOR" ) {
    val ctreMotorController = ctreMotorController(id)

    var master: MotorController<*>? = null
            internal set(value) {
                if (value != null) {
                    ctreMotorController.follow(value.ctreMotorController)
                }
                field = value
            }

    val followers = with(mutableMapOf<MotorId, MotorController<*>>()) {
        followerIds.forEach {
            set(it, MotorController(it).apply {
                master = this@MotorController
            })
        }
        toMap()
    }

    fun eachMotor(configure: MotorController<*>.() -> Unit) {
        apply(configure)
        followers.forEach { it.value.apply(configure) }
    }

    fun eachFollower(action: (MotorController<*>) -> Unit) {
        followers.forEach { action(it.value) }
    }

    fun eachTalon(configure: MotorController<TalonId>.() -> Unit) {
        eachMotor {
            @Suppress("unchecked_cast") // Will work because type of id is T
            if (id is TalonId) (this as MotorController<TalonId>).apply(configure)
        }
    }

    fun eachVictor(configure: MotorController<VictorId>.() -> Unit) {
        eachMotor {
            @Suppress("unchecked_cast") // Will work because type of id is T
            if (id is VictorId) (this as MotorController<VictorId>).apply(configure)
        }
    }

    val controlMode: ControlMode
            get() = when (ctreMotorController.controlMode) {
                CtreControlMode.PercentOutput -> ControlMode.PERCENT_OUTPUT
                CtreControlMode.Position -> ControlMode.POSITION
                CtreControlMode.Velocity -> ControlMode.VELOCITY
                CtreControlMode.Current -> ControlMode.CURRENT
                CtreControlMode.Disabled -> ControlMode.DISABLED
                else -> throw IllegalStateException("Invalid control mode.")
            }

    var neutralMode: NeutralMode = NeutralMode.COAST
            set(value) {
                eachMotor {
                    ctreMotorController.setNeutralMode(when (value) {
                        NeutralMode.COAST -> CtreNeutralMode.Coast
                        NeutralMode.BREAK -> CtreNeutralMode.Brake
                    })
                }
                field = value
            }

    init {
        eachMotor { ctreMotorController.setNeutralMode(CtreNeutralMode.Coast) }
    }

    var pidSlot: Int = 0
            set(value) {
                ctreMotorController.selectProfileSlot(value, 0)
                field = value
            }

    fun pidf(slot: Int = pidSlot, configure: MotorPidfConfigure.() -> Unit) {
        with(MotorPidfConfigure().apply(configure)) {
            ctreMotorController.apply {
                config_kP(slot, kp)
                config_kI(slot, ki)
                config_kD(slot, kd)
                config_kF(slot, kf)
                config_IntegralZone(slot, integralZone)
                configAllowableClosedloopError(slot, allowedError)
                configMaxIntegralAccumulator(slot, maxIntegral)
                configClosedLoopPeakOutput(slot, maxOutput)
                configClosedLoopPeriod(slot, period)
            }
        }
    }

    var inverted: Boolean
            get() = ctreMotorController.inverted
            set(value) {
                ctreMotorController.inverted = value
            }

    var sensorInverted: Boolean = false
            set(value) {
                ctreMotorController.setSensorPhase(value)
                field = value
            }

    val percentOutput: Double
            get() = ctreMotorController.motorOutputPercent

    var position: Int
            get() = ctreMotorController.getSelectedSensorPosition(0)
            set(value) {
                ctreMotorController.selectedSensorPosition = value
            }

    val velocity: Int
            get() = ctreMotorController.getSelectedSensorVelocity(0)

    val encoderTicks = EncoderTicks(4096)
    val encoderTicksPerSecond = encoderTicks / Seconds

    fun setPercentOutput(output: Double) {
        ctreMotorController.set(CtreControlMode.PercentOutput, output)
    }

    fun setPosition(position: Double) {
        ctreMotorController.set(CtreControlMode.Position, position)
    }

    fun <U : MetricUnit<Angular>> setPosition(position: MetricValue<Angular, U>) {
        setPosition(position.convertTo(encoderTicks).value)
    }

    fun setVelocity(velocity: Double) {
        ctreMotorController.set(CtreControlMode.Velocity, velocity)
    }

    fun <U : MetricUnit<CompositeUnitType<Per, Angular, Chronic>>> setVelocity(
            velocity: MetricValue<CompositeUnitType<Per, Angular, Chronic>, U>
    ) {
        setVelocity(velocity.convertTo(encoderTicksPerSecond).value)
    }

    fun setCurrent(current: Double) {
        ctreMotorController.set(CtreControlMode.Current, current)
    }

    fun disable() {
        ctreMotorController.neutralOutput()
    }
}

// Current can only be read from talons
val MotorController<TalonId>.current: Double
        get() = (ctreMotorController as CtreTalon).outputCurrent

enum class ControlMode {
    PERCENT_OUTPUT,
    POSITION,
    VELOCITY,
    CURRENT,
    DISABLED
}

enum class NeutralMode {
    BREAK,
    COAST
}

class MotorPidfConfigure : PidfConfigure() {
    var integralZone: Int = 0
    var allowedError: Int = 0
    var maxIntegral: Double = 0.0
    var maxOutput: Double = 0.0
    var period: Int = 0
}
