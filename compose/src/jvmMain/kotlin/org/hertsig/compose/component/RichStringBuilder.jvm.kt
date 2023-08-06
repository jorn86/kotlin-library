package org.hertsig.compose.component

import androidx.compose.ui.text.SpanStyle
import java.awt.Desktop
import java.net.URI

private val desktop = if (Desktop.isDesktopSupported()) Desktop.getDesktop() else null

actual fun RichStringBuilder.link(text: String, uri: String, id: String, style: SpanStyle): RichStringBuilder {
    require(desktop != null) { "Cannot add default link: Desktop not supported" }
    return clickableText(text, id, style) { desktop.browse(URI(uri)) }
}
