package algoritmo.extension

import java.awt.Point

fun MutableList<Point>.addIfNonexistent(item: Point) {
    if(!contains(item)) add(item)
}