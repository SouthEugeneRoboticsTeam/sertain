package org.sertain.events

interface Event {
    abstract class Targeted<T>(val target: T) : Event
}
