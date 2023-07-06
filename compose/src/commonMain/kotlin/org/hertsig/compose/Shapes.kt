package org.hertsig.compose

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class ScaledShape(
    private val scale: Float = 1f,
    private val builder: ScaledPath.(layoutDirection: LayoutDirection) -> Unit
): Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val path = Path()
        ScaledPath(path, size * scale).builder(layoutDirection)
        path.close()
        return Outline.Generic(path)
    }
}

class ScaledPath(private val path: Path, private val size: Size) {
    fun lineTo(x: Float, y: Float) = path.lineTo(x * size.width, y * size.height)
    fun moveTo(x: Float, y: Float) = path.moveTo(x * size.width, y * size.height)
    // TODO support more Path methods when needed
}

fun polygon(sides: Int, rotation: Float = 0f, scale: Float = 1f): Shape = ScaledShape(scale) {
    val angle = 2.0 * PI / sides
    val r = rotation * (PI / 180)
    moveTo(
        .5f + (.5f * cos(r).toFloat()),
        .5f + (.5f * sin(r).toFloat())
    )
    for (i in 1 until sides) {
        lineTo(
            .5f + (.5f * cos(angle * i + r).toFloat()),
            .5f + (.5f * sin(angle * i + r).toFloat())
        )
    }
}
