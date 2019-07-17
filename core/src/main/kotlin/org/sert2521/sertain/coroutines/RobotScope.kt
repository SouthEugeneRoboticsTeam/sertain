package org.sert2521.sertain.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

open class RobotScope : CoroutineScope {
    override val coroutineContext: CoroutineContext = RobotDispatcher

    companion object : RobotScope()
}
