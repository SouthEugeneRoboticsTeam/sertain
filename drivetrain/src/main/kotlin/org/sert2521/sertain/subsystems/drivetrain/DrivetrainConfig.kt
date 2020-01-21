package org.sert2521.sertain.subsystems.drivetrain

import com.kauailabs.navx.frc.AHRS
import edu.wpi.first.wpilibj.I2C
import edu.wpi.first.wpilibj.SPI
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

sealed class NavxPort

object Kmpx : NavxPort()
object KOnboard : NavxPort()

class DrivetrainConfig {
    internal val map = mutableMapOf<String, Any?>()

    var navx: AHRS? by DrivetrainConfigProperty(null)
}

class DrivetrainConfigProperty<T>(initialValue: T) : ReadWriteProperty<DrivetrainConfig, T> {
    override fun getValue(thisRef: DrivetrainConfig, property: KProperty<*>): T {
        thisRef.map[property.name].let {
            if (it !is T) {

            }
        }
    }

    override fun setValue(thisRef: DrivetrainConfig, property: KProperty<*>, value: T) {
        thisRef.map[property.name] = value
    }

}
