package org.sert2521.sertain.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.sert2521.sertain.events.Change
import org.sert2521.sertain.events.False
import org.sert2521.sertain.events.True
import org.sert2521.sertain.events.fire
import org.sert2521.sertain.events.subscribe
import org.sert2521.sertain.events.subscribeBetween
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

abstract class Observable<T>(val get: () -> T) : ReadOnlyProperty<Any?, T> {
    val value get() = get()

    var lastValue = value
        private set

    init {
        RobotScope.launch {
            periodic {
                when {
                    lastValue != value -> fire(Change(this@Observable, value))
                }
                lastValue = value
            }
        }
    }

    fun CoroutineScope.onChange(action: suspend CoroutineScope.(event: Change<T>) -> Unit) =
            subscribe(target = this@Observable, action = action)

    override fun getValue(thisRef: Any?, property: KProperty<*>) = value
}

open class ObservableValue<T>(get: () -> T) : Observable<T>(get) {
    open operator fun invoke(configure: Observable<T>.() -> Unit) = apply(configure)
}

class ObservableBoolean(get: () -> Boolean) : Observable<Boolean>(get) {
    init {
        RobotScope.onChange {
            when (it.value) {
                true -> fire(True(this@ObservableBoolean, it.value))
                false -> fire(False(this@ObservableBoolean, it.value))
            }
        }
    }

    operator fun invoke(configure: ObservableBoolean.() -> Unit) = apply(configure)

    fun CoroutineScope.onTrue(action: suspend CoroutineScope.(event: True) -> Unit) =
            subscribe(target = this@ObservableBoolean as Observable<Boolean>, action = action)

    fun CoroutineScope.onFalse(action: suspend CoroutineScope.(event: False) -> Unit) =
            subscribe(target = this@ObservableBoolean as Observable<Boolean>, action = action)

    fun CoroutineScope.whenTrue(action: suspend CoroutineScope.(event: True) -> Unit) =
            subscribeBetween<Observable<Boolean>, True, False>(target = this@ObservableBoolean, action = action)

    fun CoroutineScope.whenFalse(action: suspend CoroutineScope.(event: False) -> Unit) =
            subscribeBetween<Observable<Boolean>, False, True>(target = this@ObservableBoolean, action = action)
}

fun (() -> Boolean).watch(configure: ObservableBoolean.() -> Unit = {}) = ObservableBoolean(this).apply(configure)
fun <T> (() -> T).watch(configure: ObservableValue<T>.() -> Unit = {}) = ObservableValue(this).apply(configure)
