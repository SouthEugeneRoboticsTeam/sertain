package org.sert2521.sertain.telemetry

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser

fun <T> select(default: Pair<String, T>, vararg options: Pair<String, T>): SendableChooser<T> {
    val s = SendableChooser<T>()
    s.setDefaultOption(default.first, default.second)
    options.forEach {
        s.addOption(it.first, it.second)
    }
    return s
}
