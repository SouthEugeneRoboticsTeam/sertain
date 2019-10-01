package org.sert2521.sertain.pathmaker

import org.sert2521.sertain.pathmaker.views.MainView
import tornadofx.App
import tornadofx.launch

class PathMaker : App(MainView::class)

fun main() {
    launch<PathMaker>()
}
