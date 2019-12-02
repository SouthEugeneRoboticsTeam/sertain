package org.sert2521.sertain.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

object RobotScope : CoroutineScope {
    override val coroutineContext: CoroutineContext = RobotDispatcher
}
