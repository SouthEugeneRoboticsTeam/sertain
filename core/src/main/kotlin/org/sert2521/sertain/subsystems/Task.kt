package org.sert2521.sertain.subsystems

class Task(val action: suspend () -> Unit, val important: Boolean = true)
