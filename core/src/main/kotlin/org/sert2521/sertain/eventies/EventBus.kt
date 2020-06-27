package org.sert2521.sertain.eventies

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.sert2521.sertain.coroutines.RobotScope
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import kotlin.reflect.KClass

@OptIn(ExperimentalStdlibApi::class)
object EventBus {
    val subscribers = ArrayDeque<Sub<*>>()
    val events = ArrayDeque<Event>()

    val x = ConcurrentHashMap<KClass<Event>, ArrayDeque<Sub<Event>>>()

    fun fire(event: Event) {
        events.addLast(event)

    }

    inline fun <reified E : Event> subscribe(noinline action: suspend CoroutineScope.(E) -> Unit): Sub<E> {
        val sub = Sub(E::class, action)
        subscribers.addLast(sub)
        return sub
    }

    fun remove(sub: Sub<*>) {
        subscribers.remove(sub)
    }

    fun tick() {
        for (e in events) {
            for (s in x[e::class] ?: break) {
                RobotScope.launch {
                    s.action(this, e)
                }
            }
        }
        events.clear()
    }
}
