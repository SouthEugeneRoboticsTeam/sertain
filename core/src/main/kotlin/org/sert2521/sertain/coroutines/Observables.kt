package org.sert2521.sertain.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.sert2521.sertain.events.Event
import org.sert2521.sertain.events.Sub
import org.sert2521.sertain.events.fire
import org.sert2521.sertain.events.subscribe
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class Change<T>(target: Observable<T>, val value: T) : Event.Targeted<Observable<T>>(target)

class Observable<T>(val get: () -> T, internal val scope: CoroutineScope) : ReadOnlyProperty<Any?, T> {
    val value get() = get()

    var lastValue = value
        private set

    init {
        scope.launch {
            periodic {
                when {
                    lastValue != value -> fire(Change(this@Observable, value))
                }
                lastValue = value
            }
        }
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>) = value
}

fun <T> CoroutineScope.watch(get: () -> T) = Observable(get, this)

fun <T> Observable<T>.onChange(action: suspend CoroutineScope.(event: Change<T>) -> Unit) =
        scope.subscribe(this, action)

fun Observable<Boolean>.onTrue(action: suspend CoroutineScope.(event: Change<Boolean>) -> Unit) {
    scope.subscribe(object : Sub<Change<Boolean>> {
        override val action = action
        override fun requires(e: Event) = e is Change<*> && e.value == true
    })
}

fun Observable<Boolean>.onFalse(action: suspend CoroutineScope.(event: Change<Boolean>) -> Unit) {
    scope.subscribe(object : Sub<Change<Boolean>> {
        override val action = action
        override fun requires(e: Event) = e is Change<*> && e.value == false
    })
}
