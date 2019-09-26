package org.sert2521.pathmaker

import javafx.stage.Stage
import org.sert2521.pathmaker.views.MainView
import tornadofx.*

class PathMaker : App(MainView::class) {
    override fun start(stage: Stage) {
        super.start(stage)
        stage.width = 640.0
        stage.height = 480.0
    }

}

fun main() {
    launch<PathMaker>()
}
