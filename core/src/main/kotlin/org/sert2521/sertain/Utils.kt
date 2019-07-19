package org.sert2521.sertain

import org.sert2521.sertain.events.Tick
import org.sert2521.sertain.events.subscribe

fun onTick(action: suspend (Tick) -> Unit) {
    subscribe(action)
}
