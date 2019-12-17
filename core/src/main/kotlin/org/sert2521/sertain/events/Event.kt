package org.sert2521.sertain.events

interface Event

abstract class TargetedEvent<T>(val target: T) : Event
