package org.sert2521.sertain.utils

fun Double.deadband(tolerance: Double): Double = if (this < tolerance && this > -tolerance) {
    0.0
} else {
    this
}
