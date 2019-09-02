package org.sert2521.sertain.pathfinding

class Path2D(val name: String, ) {

}

fun path(name: String, configure: Path2DConfigure.() -> Unit): Path2D {
    with(Path2DConfigure(name).apply(configure)) {
        return Path2D(
                // TODO insert args
        )
    }
}
