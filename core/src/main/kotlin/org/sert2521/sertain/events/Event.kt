package org.sert2521.sertain.events

interface Event {
    abstract class Targeted<T>(val target: T) : Event
}
