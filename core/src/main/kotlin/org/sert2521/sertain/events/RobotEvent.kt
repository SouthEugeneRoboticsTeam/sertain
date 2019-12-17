package org.sert2521.sertain.events

interface RobotEvent : Event
interface TeleopOver : RobotEvent

object Connect : RobotEvent
object Enable : RobotEvent
object Disable : RobotEvent, TeleopOver
object Teleop : RobotEvent
object Auto : RobotEvent, TeleopOver
object Test : RobotEvent, TeleopOver
object Tick : RobotEvent
