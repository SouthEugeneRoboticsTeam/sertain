package org.sert2521.sertain.eventies

interface Event {
    abstract class Targeted<T>(val target: T) : Event
}
