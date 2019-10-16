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
        // Example code
        canvas(width = 640.0, height = 480.0) {
            val spline = createSpline(
                    Waypoint(0.0, 100.0, 0.0),
                    Waypoint(500.0, 460.0, PI / 3),
                    Quintic
            )
            graphicsContext2D.strokeText(spline.pointAt(100.0).toString(), 100.0, 100.0)
            graphicsContext2D.strokeText(spline.pointAt(0.0).toString(), 100.0, 110.0)
            graphicsContext2D.stroke = Color.BLUE
            graphicsContext2D.lineWidth = 1.0
            //graphicsContext2D.strokeLine(100.0, 100.0, 200.0, 450.0)
            graphicsContext2D.render(spline)
        }
    }
}
