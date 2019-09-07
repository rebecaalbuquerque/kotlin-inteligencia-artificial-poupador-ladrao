package algoritmo.extension

import java.awt.Point

fun MutableList<Point>.addIfNonexistent(item: Point) {
    if(!contains(item)) add(item)
}

fun MutableList<Point>.removeIfExists(item: Point) {
    if(!contains(item)) remove(item)
}