package org.sert2521.sertain.pathmaker.views

import tornadofx.View
import tornadofx.hbox

class MainView : View() {
    val display by inject<DisplayView>()

    override val root = hbox {
        add(display)
    }
}
