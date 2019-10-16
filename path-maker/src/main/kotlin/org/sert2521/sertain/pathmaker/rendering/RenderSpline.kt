package org.sert2521.sertain.pathmaker.rendering

import javafx.scene.canvas.GraphicsContext
import org.sert2521.sertain.pathfinding.Point
import org.sert2521.sertain.pathfinding.Spline

fun GraphicsContext.render(spline: Spline) {
    var p = 0.001
    var lastPoint = spline.pointAt(0.0)
    while (p <= 1.0) {
        val point = spline.pointAt(p)
        strokeLine(lastPoint.x, lastPoint.y, point.x, point.y)
        lastPoint = point
        p += 0.001
    }
}
