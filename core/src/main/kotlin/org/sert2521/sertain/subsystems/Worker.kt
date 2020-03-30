package org.sert2521.sertain.subsystems

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import org.sert2521.sertain.coroutines.RobotScope
import kotlin.reflect.full.createInstance

abstract class Worker<T> {
    abstract val value: T
    abstract val name: String
    abstract var isEnabled: Boolean

    abstract val default: (suspend CoroutineScope.(s: T) -> Any?)?

    abstract val setup: (CoroutineScope.(s: T) -> Any?)?

    suspend operator fun <R> invoke(action: suspend CoroutineScope.(T) -> R) = use(this, action = action)

    internal var currentJob: Job? = null

    val occupied: Boolean
        get() = currentJob != null
}

open class WorkerConfig<T> {
    var default: (suspend CoroutineScope.(s: T) -> Any?)? = null
    var defaultScope: CoroutineScope = RobotScope

    fun default(scope: CoroutineScope = RobotScope, action: suspend CoroutineScope.(s: T) -> Any?) {
        default = action
        defaultScope = scope
    }

    var setup: (CoroutineScope.(s: T) -> Any?)? = null

    fun setup(action: CoroutineScope.(s: T) -> Any?) {
        setup = action
    }

    var isEnabled = true
}

fun <T> worker(value: T, name: String = "Unnamed Subsystem", configure: WorkerConfig<T>.() -> Unit = {}): Worker<T> {
    val config = WorkerConfig<T>().apply(configure)
    val worker = object : Worker<T>() {
        override val value = value
        override val name = name
        override var isEnabled = config.isEnabled
        override val default = config.default
        override val setup = config.setup
    }
    Workers.add(worker)
    return worker
}

inline fun <reified S : Subsystem> add(): Worker<S> {
    val subsystem = S::class.createInstance()
    val worker = object : Worker<S>() {
        override val value = subsystem
        override val name = subsystem.name ?: subsystem::class.simpleName ?: "Unnamed Subsystem"
        override var isEnabled: Boolean
            get() = subsystem.enabled
            set(value) {
                subsystem.enabled = value
            }
        override val default: (suspend CoroutineScope.(s: S) -> Any?)?
            get() = { subsystem.apply { default() } }
        override val setup: (CoroutineScope.(s: S) -> Any?)?
            get() = { subsystem.apply { setup() } }
    }
    Workers.add(worker)
    return worker
}
