package org.sert2521.sertain.motors

import com.ctre.phoenix.motorcontrol.ControlMode as PhoenixControlMode
import com.ctre.phoenix.motorcontrol.can.VictorSPX

class VictorMotorController(
        id: VictorId,
        vararg followerIds: VictorId,
        configure: MotorController.() -> Unit = {}
) : MotorController() {
    val spx = VictorSPX(id.number)

    var master: MotorController? = null
        set(value) {
            if (value != null) {
                spx.follow(value.baseController!!)
            }
            field = value
        }

    val followers: MutableMap<MotorId, MotorController> = with(mutableMapOf<MotorId, MotorController>()) {
        followerIds.forEach {
            set(it, motorController(it).apply {
                master = this
            })
        }
        toMutableMap()
    }

    override val baseController = spx

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
        get() = when (spx.controlMode) {
            PhoenixControlMode.PercentOutput -> ControlMode.PERCENT_OUTPUT
            PhoenixControlMode.Position -> ControlMode.POSITION
            PhoenixControlMode.Velocity -> ControlMode.VELOCITY
            PhoenixControlMode.Current -> ControlMode.CURRENT
            PhoenixControlMode.Disabled -> ControlMode.DISABLED
            else -> throw IllegalStateException("Invalid control mode.")
        }
    override var brakeMode: Boolean = false
        set(value) {
            eachFollower {
                brakeMode = value
            }
            spx.setNeutralMode(ctreNeutralMode(value))
            field = value
        }
    override var pidfSlot: Int = 0
        set(value) {
            spx.selectProfileSlot(value, 0)
            field = value
        }

    override var inverted: Boolean
        get() = spx.inverted
        set(value) {
            spx.inverted = value
            eachFollower {
                inverted = value
            }
        }
    override var sensorInverted: Boolean = false
        set(value) {
            spx.setSensorPhase(value)
            field = value
        }

    override var openLoopRamp: Double = 0.0
        set(value) {
            spx.configOpenloopRamp(value, 20)
        }

    override var closedLoopRamp: Double = 0.0
        set(value) {
            spx.configClosedloopRamp(value, 20)
        }

    override var minOutputRange: ClosedRange<Double> = 0.0..0.0
        set(value) {
            spx.configNominalOutputForward(value.endInclusive, 20)
            spx.configNominalOutputReverse(value.endInclusive, 20)
            field = value
        }

    override var maxOutputRange: ClosedRange<Double> = -1.0..1.0
        set(value) {
            spx.configPeakOutputForward(value.endInclusive, 20)
            spx.configPeakOutputReverse(value.start, 20)
            field = value
        }

    override val percentOutput: Double
        get() = spx.motorOutputPercent

    override var position: Int
        get() = spx.getSelectedSensorPosition(0)
        set(value) {
            spx.selectedSensorPosition = value
        }

    override val velocity: Int
        get() = spx.getSelectedSensorVelocity(0)

    override fun setPercentOutput(output: Double) {
        spx.set(PhoenixControlMode.PercentOutput, output)
    }

    override fun setTargetPosition(position: Int) {
        spx.set(PhoenixControlMode.Position, position.toDouble())
    }

    override fun setTargetVelocity(velocity: Int) {
        spx.set(PhoenixControlMode.Velocity, velocity.toDouble())
    }

    override fun setCurrent(current: Double) {
        spx.set(PhoenixControlMode.Current, current)
    }

    override fun disable() {
        spx.neutralOutput()
    }

    override fun updatePidf(slot: Int, pidf: MotorPidf) {
        with(pidf) {
            spx.apply {
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

    init {
        eachMotor { spx.setNeutralMode(ctreNeutralMode(brakeMode)) }
        spx.apply {
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
        }
        configure()
    }
}
