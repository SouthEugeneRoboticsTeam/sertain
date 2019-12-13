package org.sert2521.sertain.motors

import org.sert2521.sertain.units.Angular
import org.sert2521.sertain.units.Chronic
import org.sert2521.sertain.units.CompositeUnit
import org.sert2521.sertain.units.CompositeUnitType
import org.sert2521.sertain.units.MetricUnit
import org.sert2521.sertain.units.MetricValue
import org.sert2521.sertain.units.Per
import org.sert2521.sertain.units.convertTo
import java.lang.NullPointerException
import com.ctre.phoenix.motorcontrol.ControlMode as CtreControlMode
import com.ctre.phoenix.motorcontrol.NeutralMode as CtreNeutralMode
import com.ctre.phoenix.motorcontrol.can.TalonSRX as CtreTalon

class MotorController<T : MotorId>(
    val id: T,
    vararg followerIds: MotorId,
    configure: MotorController<T>.() -> Unit = {}
) {
    val ctreMotorController = ctreMotorController(id)

    var encoder: Encoder? = null

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
        eachFollower(configure)
    }

    fun eachFollower(configure: MotorController<*>.() -> Unit) {
        followers.forEach { it.value.apply(configure) }
    }

    fun eachTalon(configure: MotorController<TalonId>.() -> Unit) {
        eachMotor {
            @Suppress("unchecked_cast") // Will work because type of id is T
            (this as? MotorController<TalonId>)?.apply(configure)
        }
    }

    fun eachVictor(configure: MotorController<VictorId>.() -> Unit) {
        eachMotor {
            @Suppress("unchecked_cast") // Will work because type of id is T
            (this as? MotorController<VictorId>)?.apply(configure)
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

    var brakeMode: Boolean = false
            set(value) {
                eachFollower {
                    brakeMode = value
                }
                ctreMotorController.setNeutralMode(ctreNeutralMode(value))
                field = value
            }

    var pidfSlot: Int = 0
            set(value) {
                ctreMotorController.selectProfileSlot(value, 0)
                field = value
            }

    val pidf = MotorPidfCollection(this, 0 to MotorPidf())

    fun pidf(slot: Int = pidfSlot, configure: MotorPidfConfig.() -> Unit) {
        with(MotorPidfConfig().apply(configure)) {
            pidf[slot] = MotorPidf(
                kp, ki, kd, kf,
                integralZone,
                allowedError,
                maxIntegral,
                maxOutput,
                period
            )
        }
    }

    var currentLimit = CurrentLimit(0, 0, 0, false)
            set(value) {
                updateCurrentLimit(value)
                field = value
            }

    fun currentLimit(configure: CurrentLimitConfigure.() -> Unit) {
        with(CurrentLimitConfigure().apply(configure)) {
            currentLimit = CurrentLimit(continuousLimit, maxLimit, maxDuration, enabled)
        }
    }

    var inverted: Boolean
            get() = ctreMotorController.inverted
            set(value) {
                ctreMotorController.inverted = value
                eachFollower {
                    inverted = value
                }
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

    var openLoopRamp: Double = 0.0
            set(value) {
                ctreMotorController.configOpenloopRamp(value, 20)
            }

    var closedLoopRamp: Double = 0.0
            set(value) {
                ctreMotorController.configClosedloopRamp(value, 20)
            }

    var minOutputRange: ClosedRange<Double> = 0.0..0.0
            set(value) {
                ctreMotorController.configNominalOutputForward(value.endInclusive, 20)
                ctreMotorController.configNominalOutputReverse(value.endInclusive, 20)
                field = value
            }

    var maxOutputRange: ClosedRange<Double> = -1.0..1.0
            set(value) {
                ctreMotorController.configPeakOutputForward(value.endInclusive, 20)
                ctreMotorController.configPeakOutputReverse(value.start, 20)
                field = value
            }

    fun setPercentOutput(output: Double) {
        ctreMotorController.set(CtreControlMode.PercentOutput, output)
    }

    fun setPosition(position: Int) {
        ctreMotorController.set(CtreControlMode.Position, position.toDouble())
    }

    fun <U : MetricUnit<Angular>> setPosition(position: MetricValue<Angular, U>) {
        try {
            setPosition(position.convertTo(encoder!!.ticks).value.toInt())
        } catch (e: NullPointerException) {
            throw java.lang.IllegalStateException(
                    "You must configure your encoder to use units."
            )
        }
    }

    fun position(unit: MetricUnit<Angular>) =
        MetricValue(encoder!!.ticks, position.toDouble()).convertTo(unit)

    fun setVelocity(velocity: Double) {
        ctreMotorController.set(CtreControlMode.Velocity, velocity)
    }

    fun setVelocity(
        velocity: MetricValue<CompositeUnitType<Per, Angular, Chronic>, CompositeUnit<Per, Angular, Chronic>>
    ) {
        try {
            setVelocity(velocity.convertTo(encoder!!.ticksPerSecond).value)
        } catch (e: NullPointerException) {
            throw java.lang.IllegalStateException(
                    "You must configure your encoder to use units."
            )
        }
    }

    fun velocity(unit: CompositeUnit<Per, Angular, Chronic>) =
        MetricValue(encoder!!.ticksPerSecond, velocity.toDouble()).convertTo(unit)

    fun setCurrent(current: Double) {
        ctreMotorController.set(CtreControlMode.Current, current)
    }

    fun disable() {
        ctreMotorController.neutralOutput()
    }

    internal fun updatePidf(slot: Int, pidf: MotorPidf) {
        with(pidf) {
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

    private fun updateCurrentLimit(limit: CurrentLimit) {
        with(limit) {
            eachTalon {
                (ctreMotorController as CtreTalon).apply {
                    configContinuousCurrentLimit(currentLimit.continuousLimit)
                    configPeakCurrentLimit(currentLimit.maxLimit)
                    configPeakCurrentDuration(currentLimit.maxDuration)
                    enableCurrentLimit(currentLimit.enabled)
                }
            }
        }
    }

    init {
        eachMotor { ctreMotorController.setNeutralMode(ctreNeutralMode(brakeMode)) }
        ctreMotorController.apply {
            configClosedloopRamp(closedLoopRamp)
            configOpenloopRamp(openLoopRamp)
            configNominalOutputReverse(minOutputRange.start)
            configNominalOutputForward(minOutputRange.endInclusive)
            configPeakOutputReverse(maxOutputRange.start)
            configPeakOutputForward(maxOutputRange.endInclusive)
            selectProfileSlot(pidfSlot, 0)
            pidf.toMap().forEach {
                updatePidf(it.key, it.value)
            }
            updateCurrentLimit(currentLimit)
        }
        eachMotor {
            ctreMotorController.setNeutralMode(ctreNeutralMode(brakeMode))
        }
        configure()
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
    BRAKE,
    COAST
}

fun ctreNeutralMode(brakeMode: Boolean): CtreNeutralMode =
        if (brakeMode) CtreNeutralMode.Brake else CtreNeutralMode.Coast
