package org.sert2521.sertain.eventies

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.sert2521.sertain.coroutines.RobotScope

@OptIn(ExperimentalStdlibApi::class)
object EventBus {
    val subscribers = ArrayDeque<Sub<*>?>()
    val events = ArrayDeque<Event?>()

    fun fire(event: Event) {
        events.addLast(event)
    }

    fun <E : Event> subscribe(s: Sub<E>): Sub<E> {
        subscribers.addLast(s)
        return s
    }

    inline fun <reified E : Event> subscribe(noinline action: suspend CoroutineScope.(E) -> Unit): Sub<E> {
        val sub = object : Sub<E> {
            override val action = action
            override fun requires(e: Event) = e is E
        }
        subscribers.addLast(sub)
        return sub
    }

    inline fun <reified E : Event.Targeted<*>> subscribe(target: Any?, noinline action: suspend CoroutineScope.(E) -> Unit): Sub<E> {
        val sub = object : Sub<E> {
            override val action = action
            override fun requires(e: Event) = e is E && e.target == target
        }
        subscribers.addLast(sub)
        return sub
    }

    inline fun <reified E1 : Event, reified E2 : Event> between(noinline action: suspend CoroutineScope.(E1) -> Unit): Sub<E1> {
        return subscribe scope@{
            subscribe<E2> {
                this@scope.cancel()
            }
            action(this, it)
        }
    }

    inline fun <reified E1 : Event.Targeted<*>, reified E2 : Event.Targeted<*>> between(target: Any?, noinline action: suspend CoroutineScope.(E1) -> Unit): Sub<E1> {
        return subscribe(target) scope@{
            subscribe<E2>(target) {
                this@scope.cancel()
            }
            action(this, it)
        }
    }

    fun remove(sub: Sub<*>) {
        subscribers.remove(sub)
    }

    suspend fun tick() {
        for (e in events) {
            if (e != null) {
                for (s in subscribers) {
                    if (s != null && s.requires(e)) {
                        @Suppress("UNCHECKED_CAST")
                        RobotScope.launch {
                            (s as Sub<Event>).action(this, e)
                        }
                    }
                }
            }
        }
        events.clear()
    }
}
