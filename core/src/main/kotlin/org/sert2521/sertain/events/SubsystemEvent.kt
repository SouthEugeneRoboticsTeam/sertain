package org.sert2521.sertain.events

import org.sert2521.sertain.subsystems.Subsystem
import org.sert2521.sertain.subsystems.Task

abstract class TaskEvent : Event()

class Use(val subsystems: Set<Subsystem>, val important: Boolean, val action: suspend () -> Unit) : TaskEvent()
