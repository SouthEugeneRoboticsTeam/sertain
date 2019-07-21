package org.sert2521.sertain.events

sealed class RobotEvent : Event()

object Connect : RobotEvent()
object Enable : RobotEvent()
object Disable : RobotEvent()
object Teleop : RobotEvent()
object Auto : RobotEvent()
object Test : RobotEvent()
object Tick : RobotEvent()
