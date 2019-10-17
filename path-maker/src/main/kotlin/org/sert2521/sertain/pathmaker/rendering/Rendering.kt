package org.sert2521.sertain.pathmaker.rendering

import javafx.scene.canvas.GraphicsContext
import org.sert2521.sertain.pathfinding.*

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

fun GraphicsContext.render(type: SplineType, vararg waypoints: Waypoint) {
    val splines = mutableListOf<Spline>()
    waypoints.reduce { wp1: Waypoint, wp2: Waypoint ->
        splines += createSpline(wp1, wp2, type)
        wp2
    }
    splines.forEach {
        render(it)
    }
}
