package org.sert2521.example

import edu.wpi.first.wpilibj.Joystick
import edu.wpi.first.wpilibj.XboxController
import kotlinx.coroutines.CoroutineScope
import java.lang.IllegalArgumentException
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

data class Control<T, O>(
    val option: O,
    val get: () -> T
)

class Mapping<T, O>(option: O, vararg controls: Control<T, O>) : ReadOnlyProperty<Any?, T> {
    private val selected = controls.singleOrNull { it.option == option } ?:
        throw IllegalArgumentException("Argument `controls` must contain exactly one `Control` with the desired option.")

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return selected.get()
    }
}

object Controls {
    enum class ControlOption2 {
        JOYSTICK, CONTROLLER
    }

    val secondaryControl = ControlOption2.CONTROLLER

    private val joystick1 = Joystick(0)
    private val joystick2 = Joystick(1)
    private val controller2 = XboxController(2)

    val drive by Mapping(
        secondaryControl,
        Control(ControlOption2.JOYSTICK) {  }
    )
}

fun CoroutineScope.initControls() {
    ({ Controls.joystick1.getRawButton(0) })
}
