package org.sert2521.sertain.motors

import com.ctre.phoenix.motorcontrol.can.BaseMotorController
import org.sert2521.sertain.units.*

abstract class PhoenixMotorController : MotorController() {
    abstract val baseController: BaseMotorController?

    open var encoder: Encoder? = null

    abstract val controlMode: ControlMode

    open var brakeMode: Boolean = false

    open var pidfSlot: Int = 0

    val pidf = MotorPidfCollection(this, 0 to MotorPidf())

    fun pidf(slot: Int = pidfSlot, configure: MotorPidfConfig.() -> Unit) {
        with(MotorPidfConfig().apply(configure)) {
            pidf[slot] = MotorPidf(
                    kp ?: pidf[slot]?.kp ?: 0.0,
                    ki ?: pidf[slot]?.ki ?: 0.0,
                    kd ?: pidf[slot]?.kd ?: 0.0,
                    kf ?: pidf[slot]?.kf ?: 0.0,
                    integralZone ?: pidf[slot]?.integralZone ?: 0,
                    allowedError ?: pidf[slot]?.allowedError ?: 0,
                    maxIntegral ?: pidf[slot]?.maxIntegral ?: 0.0,
                    maxOutput ?: pidf[slot]?.maxOutput ?: 1.0,
                    period ?: pidf[slot]?.period ?: 0
            )
        }
    }

    open var currentLimit = CurrentLimit(0, 0, 0, false)

    fun currentLimit(configure: CurrentLimitConfigure.() -> Unit) {
        with(CurrentLimitConfigure().apply(configure)) {
            currentLimit = CurrentLimit(continuousLimit, maxLimit, maxDuration, enabled)
        }
    }

    abstract var position: Int

    abstract val velocity: Int

    override fun <U : AngularUnit> position(unit: U) =
            MetricValue(encoder!!.ticks, position.toDouble()).convertTo(unit)
    override fun <U1 : AngularVelocity, U2 : ChronicUnit, U : AngularVelocityUnit<U1, U2>> velocity(unit: U) =
            MetricValue(encoder!!.ticksPerSecond, velocity.toDouble()).convertTo(unit)

    abstract fun setTargetPosition(position: Int)

    abstract fun setTargetVelocity(velocity: Int)

    override fun <U : AngularUnit, V : AngularValue<U>> setTargetPosition(position: V) {
        checkNotNull(encoder) { "You must configure your encoder to use units." }
        setTargetPosition(position.convertTo(encoder!!.ticks).value.toInt())
    }

    override fun <U1 : AngularUnit, U2 : ChronicUnit, V : AngularVelocityValue<U1, U2>> setTargetVelocity(velocity: V) {
        checkNotNull(encoder) { "You must configure your encoder to use units." }
        setTargetVelocity(velocity.convertTo(encoder!!.ticksPerSecond).value.toInt())
    }

    abstract fun setCurrent(current: Double)
}