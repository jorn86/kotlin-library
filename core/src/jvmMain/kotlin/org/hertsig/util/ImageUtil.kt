package org.hertsig.util

import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.nio.file.Path
import javax.imageio.ImageIO
import kotlin.io.path.Path
import kotlin.io.path.extension
import kotlin.io.path.inputStream
import kotlin.io.path.outputStream

fun tileify(sourcePath: Path, targetPath: Path, space: Int = 0) {
    val source = sourcePath.inputStream().use { ImageIO.read(it) }
    val halfHeight = source.height / 2
    val halfWidth = source.width / 2
    val target = BufferedImage(source.width + space, source.height + space, source.type)
    target.withGraphics {
        drawImage(source, -halfWidth, -halfHeight, null)
        drawImage(source, halfWidth + space, -halfHeight, null)
        drawImage(source, -halfWidth, halfHeight + space, null)
        drawImage(source, halfWidth + space, halfHeight + space, null)
    }
    targetPath.outputStream().use { ImageIO.write(target, targetPath.extension, it) }
}

fun <T> BufferedImage.withGraphics(action: Graphics2D.() -> T): T {
    val graphics = createGraphics()
    try {
        return graphics.action()
    } finally {
        graphics.dispose()
    }
}
