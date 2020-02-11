package org.sert2521.sertain.motors

import com.ctre.phoenix.motorcontrol.can.TalonSRX

class TalonMotorController(
        id: TalonId,
        vararg followerIds: MotorId,
        configure: MotorController.() -> Unit = {}
) : MotorController() {
    val srx = TalonSRX(id.number)

    var master: MotorController? = null
        set(value) {
            if (value != null) {
                srx.follow(value.baseController!!)
            }
            field = value
        }

    val followers: MutableMap<MotorId, MotorController> = with(mutableMapOf<MotorId, MotorController>()) {
        followerIds.forEach {
            set(it, motorController(it)!!.apply {
                master = this
            })
        }
        toMutableMap()
    }

    override val baseController = srx

    override fun eachMotor(configure: MotorController.() -> Unit) {
        apply(configure)
        eachFollower(configure)
    }

    override fun eachFollower(configure: MotorController.() -> Unit) {
        followers.forEach {
            it.value.apply(configure)
        }
    }

    override val controlMode: ControlMode
        get() = when (srx.controlMode) {
            com.ctre.phoenix.motorcontrol.ControlMode.PercentOutput -> ControlMode.PERCENT_OUTPUT
            com.ctre.phoenix.motorcontrol.ControlMode.Position -> ControlMode.POSITION
            com.ctre.phoenix.motorcontrol.ControlMode.Velocity -> ControlMode.VELOCITY
            com.ctre.phoenix.motorcontrol.ControlMode.Current -> ControlMode.CURRENT
            com.ctre.phoenix.motorcontrol.ControlMode.Disabled -> ControlMode.DISABLED
            else -> throw IllegalStateException("Invalid control mode.")
        }
    override var brakeMode: Boolean = false
        set(value) {
            eachFollower {
                brakeMode = value
            }
            srx.setNeutralMode(ctreNeutralMode(value))
            field = value
        }
    override var pidfSlot: Int = 0
        set(value) {
            srx.selectProfileSlot(value, 0)
            field = value
        }

    override var currentLimit = CurrentLimit(0, 0, 0, false)
        set(value) {
            updateCurrentLimit(value)
            field = value
        }

    override var inverted: Boolean
        get() = srx.inverted
        set(value) {
            srx.inverted = value
            eachFollower {
                inverted = value
            }
        }
    override var sensorInverted: Boolean = false
        set(value) {
            srx.setSensorPhase(value)
            field = value
        }

    override var openLoopRamp: Double = 0.0
        set(value) {
            srx.configOpenloopRamp(value, 20)
        }

    override var closedLoopRamp: Double = 0.0
        set(value) {
            srx.configClosedloopRamp(value, 20)
        }

    override var minOutputRange: ClosedRange<Double> = 0.0..0.0
        set(value) {
            srx.configNominalOutputForward(value.endInclusive, 20)
            srx.configNominalOutputReverse(value.endInclusive, 20)
            field = value
        }

    override var maxOutputRange: ClosedRange<Double> = -1.0..1.0
        set(value) {
            srx.configPeakOutputForward(value.endInclusive, 20)
            srx.configPeakOutputReverse(value.start, 20)
            field = value
        }

    override val percentOutput: Double
        get() = srx.motorOutputPercent

    override var position: Int
        get() = srx.getSelectedSensorPosition(0)
        set(value) {
            srx.selectedSensorPosition = value
        }

    override val velocity: Int
        get() = srx.getSelectedSensorVelocity(0)

    override fun setPercentOutput(output: Double) {
        srx.set(com.ctre.phoenix.motorcontrol.ControlMode.PercentOutput, output)
    }

    override fun setTargetPosition(position: Int) {
        srx.set(com.ctre.phoenix.motorcontrol.ControlMode.Position, position.toDouble())
    }

    override fun setTargetVelocity(velocity: Int) {
        srx.set(com.ctre.phoenix.motorcontrol.ControlMode.Velocity, velocity.toDouble())
    }

    override fun setCurrent(current: Double) {
        srx.set(com.ctre.phoenix.motorcontrol.ControlMode.Current, current)
    }

    override fun disable() {
        srx.neutralOutput()
    }

    override fun updatePidf(slot: Int, pidf: MotorPidf) {
        with(pidf) {
            srx.apply {
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
        eachMotor {
            srx.apply {
                configContinuousCurrentLimit(limit.continuousLimit)
                configPeakCurrentLimit(limit.maxLimit)
                configPeakCurrentDuration(limit.maxDuration)
                enableCurrentLimit(limit.enabled)
            }
        }
    }

    init {
        eachMotor { srx.setNeutralMode(ctreNeutralMode(brakeMode)) }
        srx.apply {
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
        configure()
    }
}