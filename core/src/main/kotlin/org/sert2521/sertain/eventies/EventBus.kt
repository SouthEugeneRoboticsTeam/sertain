package org.sert2521.sertain.eventies

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.sert2521.sertain.coroutines.RobotScope

@OptIn(ExperimentalStdlibApi::class)
object EventBus {
    val subscribers = ArrayDeque<Sub<*>?>()
    val events = ArrayDeque<Event?>()

    fun fire(event: Event) {
        events.addLast(event)
    }

    inline fun <reified E : Event> subscribe(noinline action: suspend CoroutineScope.(E) -> Unit): Sub<E> {
        val sub = object : Sub<E> {
            override val action = action
            override fun requires(e: Event) = e is E
        }
        subscribers.addLast(sub)
        return sub
    }

    fun remove(sub: Sub<*>) {
        subscribers.remove(sub)
    }

    suspend fun tick() {
        for (e in events) {
            if (e != null) {
                for (s in subscribers) {
                    s?.withEventOrNull(e)?.let {
                        RobotScope.launch {
                            it.action(this, e)
                        }
                    }
                }
            }
        }
        events.clear()
    }
}
