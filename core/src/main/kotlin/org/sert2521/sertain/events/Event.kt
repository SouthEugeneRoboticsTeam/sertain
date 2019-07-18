package org.sert2521.sertain.events

abstract class Event


abstract class TargetedEvent<T>(val target: T) : Event()
