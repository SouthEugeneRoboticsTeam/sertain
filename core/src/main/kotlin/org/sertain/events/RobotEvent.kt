package org.sertain.events

interface RobotEvent : Event

interface TeleopOver : RobotEvent
interface AutoOver : RobotEvent
interface TestOver : RobotEvent

object Connect : RobotEvent
object Start : RobotEvent
object Enable : RobotEvent
object Disable : RobotEvent, TeleopOver, AutoOver, TestOver
object Teleop : RobotEvent, AutoOver, TestOver
object Auto : RobotEvent, TeleopOver, TestOver
object Test : RobotEvent, TeleopOver, AutoOver
