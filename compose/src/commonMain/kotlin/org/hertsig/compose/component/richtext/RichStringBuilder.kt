package org.hertsig.compose.component.richtext

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.TextUnit
import org.hertsig.compose.util.Content
import org.hertsig.compose.util.dpToSp
import org.hertsig.util.putFirst

val LINK_STYLE = SpanStyle(Color.Blue, textDecoration = TextDecoration.Underline)
internal val CLICKABLE_TAG = RichStringBuilder::class.qualifiedName + "-clickable"
internal val TOOLTIP_TAG = RichStringBuilder::class.qualifiedName + "-tooltip"

@Composable
fun rememberRichString(vararg keys: Any, builder: RichStringBuilder.() -> Unit) = remember(*keys) {
    RichStringBuilder().apply(builder).build()
}

@OptIn(ExperimentalTextApi::class)
open class RichStringBuilder
internal constructor() {
    private val builder = AnnotatedString.Builder()
    private val inline = mutableMapOf<String, InlineTextContent>()
    private val clickables = mutableMapOf<String, () -> Unit>()
    private val tooltips = mutableMapOf<String, Tooltip>()

    fun append(text: String): RichStringBuilder {
        builder.append(text)
        return this
    }

    fun append(text: String, style: SpanStyle): RichStringBuilder {
        builder.withStyle(style) { append(text) }
        return this
    }

    fun withStyle(style: ParagraphStyle, content: RichStringBuilder.() -> Unit): RichStringBuilder {
        builder.withStyle(style) { content() }
        return this
    }

    fun withStyle(style: SpanStyle, content: RichStringBuilder.() -> Unit): RichStringBuilder {
        builder.withStyle(style) { content() }
        return this
    }

    fun inline(
        id: String,
        width: TextUnit,
        height: TextUnit, // LocalTextStyle.current.fontSize
        align: PlaceholderVerticalAlign = PlaceholderVerticalAlign.AboveBaseline,
        content: Content,
    ): RichStringBuilder {
        inline.putFirst(id, InlineTextContent(Placeholder(width, height, align)) { content() })
        builder.appendInlineContent(id)
        return this
    }

    fun icon(
        icon: Painter,
        size: DpSize,
        density: Density, // LocalDensity.current
        id: String,
        modifier: Modifier = Modifier,
        description: String = id,
    ) = inline(id, size.width.dpToSp(density), size.height.dpToSp(density), PlaceholderVerticalAlign.Center) {
        Icon(icon, description, Modifier.size(size).then(modifier))
    }

    fun withClickable(id: String, action: () -> Unit, content: RichStringBuilder.() -> Unit): RichStringBuilder {
        clickables.putFirst(id, action)
        builder.withAnnotation(CLICKABLE_TAG, id) { content() }
        return this
    }

    fun clickableText(text: String, id: String, style: SpanStyle = SpanStyle(), action: () -> Unit) =
        withClickable(id, action) { append(text, style) }

    fun withTooltip(
        id: String,
        tooltip: Tooltip,
        content: RichStringBuilder.() -> Unit,
    ): RichStringBuilder {
        tooltips.putFirst(id, tooltip)
        builder.withAnnotation(TOOLTIP_TAG, id) { content() }
        return this
    }

    fun textWithTooltip(text: String, id: String, tooltip: Tooltip, style: SpanStyle = SpanStyle()) =
        withTooltip(id, tooltip) { append(text, style) }

    fun build() = RichString(builder.toAnnotatedString(), inline, clickables, tooltips)
}

// FIXME convert back to default parameter once that doesn't crash the compiler
fun RichStringBuilder.link(text: String, uri: String, id: String) = link(text, uri, id, LINK_STYLE)
expect fun RichStringBuilder.link(text: String, uri: String, id: String, style: SpanStyle): RichStringBuilder
