package org.sert2521.example

import edu.wpi.first.wpilibj.GenericHID
import edu.wpi.first.wpilibj.Joystick
import edu.wpi.first.wpilibj.XboxController
import org.sert2521.sertain.utils.get

object OI {
    enum class ControlOption1 {
        JOYSTICK, CONTROLLER
    }

    private val controlOption1 = ControlOption1.CONTROLLER

    private val joystick1 = Joystick(0)
    private val controller1 = XboxController(2)
    private val joystick2 = Joystick(1)

    val drive by when (controlOption1) {
        ControlOption1.JOYSTICK -> get { joystick1.y }
        ControlOption1.CONTROLLER -> get { controller1.getY(GenericHID.Hand.kLeft) }
    }

    val turn by when (controlOption1) {
        ControlOption1.JOYSTICK -> get { joystick1.x }
        ControlOption1.CONTROLLER -> get { controller1.getX(GenericHID.Hand.kLeft) }
    }
}
