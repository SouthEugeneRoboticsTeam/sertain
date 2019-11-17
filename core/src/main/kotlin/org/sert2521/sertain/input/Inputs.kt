package org.sert2521.sertain.input

import edu.wpi.first.wpilibj.DigitalInput
import edu.wpi.first.wpilibj.DriverStation
import org.sert2521.sertain.coroutines.ObservableBoolean

fun stickButton(portId: Int, buttonId: Int, configure: ObservableBoolean.() -> Unit = {}) =
        ObservableBoolean { DriverStation.getInstance().getStickButton(portId, buttonId) }.apply(configure)

fun digitalInput(dioId: Int, configure: ObservableBoolean.() -> Unit = {}) = DigitalInput(dioId).run {
    ObservableBoolean { get() }
}
