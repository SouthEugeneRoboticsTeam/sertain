package org.sert2521.sertain.pathmaker.views

import javafx.scene.paint.Color
import org.sert2521.sertain.pathfinding.Quintic
import org.sert2521.sertain.pathfinding.Waypoint
import org.sert2521.sertain.pathfinding.createSpline
import org.sert2521.sertain.pathmaker.rendering.render
import tornadofx.View
import tornadofx.canvas
import tornadofx.pane
import kotlin.math.PI

class DisplayView : View() {
    override val root = pane {
        // Example code // f //
        canvas(width = 640.0, height = 480.0) {
            graphicsContext2D.stroke = Color.BLUE
            graphicsContext2D.lineWidth = 1.0
            //graphicsContext2D.strokeLine(100.0, 100.0, 200.0, 450.0)
            graphicsContext2D.render(
                    Quintic,
                    Waypoint(100.0, 100.0, PI / 2),
                    Waypoint(120.0, 180.0, PI / 4),
                    Waypoint(200.0, 200.0, 0.0)
            )
        }
    }
}
