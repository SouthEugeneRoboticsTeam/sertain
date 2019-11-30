package org.sert2521.sertain.input

import edu.wpi.first.wpilibj.AnalogInput
import edu.wpi.first.wpilibj.DigitalInput
import edu.wpi.first.wpilibj.DriverStation
import org.sert2521.sertain.coroutines.ObservableBoolean
import org.sert2521.sertain.coroutines.ObservableValue

fun stickButton(portId: Int, buttonId: Int, configure: ObservableBoolean.() -> Unit = {}) =
        ObservableBoolean { DriverStation.getInstance().getStickButton(portId, buttonId) }.apply(configure)

fun stickAxis(portId: Int, axisId: Int) =
        ObservableValue { DriverStation.getInstance().getStickAxis(portId, axisId) }

fun digitalInput(dioId: Int, configure: ObservableBoolean.() -> Unit = {}) = DigitalInput(dioId).run {
    ObservableBoolean { get() }
}

fun analogInput(dioId: Int, configure: ObservableBoolean.() -> Unit = {}) = AnalogInput(dioId).run {
    ObservableValue { value }
}

fun analogInputAverage(dioId: Int, configure: ObservableBoolean.() -> Unit = {}) = AnalogInput(dioId).run {
    ObservableValue { averageValue }
}
