package org.sertain.motors

data class CurrentLimit(
    val continuousLimit: Int = 0,
    val maxLimit: Int = 0,
    val maxDuration: Int = 0,
    val enabled: Boolean = true
)

class CurrentLimitConfigure {
    var continuousLimit: Int = 0
    var maxLimit: Int = 0
    var maxDuration: Int = 0
    var enabled: Boolean = true
}
