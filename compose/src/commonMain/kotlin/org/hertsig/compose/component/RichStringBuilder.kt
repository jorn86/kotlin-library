package org.hertsig.compose.component

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.Icon
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.*
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.TextUnit
import org.hertsig.compose.append
import org.hertsig.compose.build
import org.hertsig.compose.dpToSp
import org.hertsig.compose.inlineText
import org.hertsig.util.putFirst

internal val CLICKABLE_TAG = RichStringBuilder::class.qualifiedName + "-clickable"
internal val TOOLTIP_TAG = RichStringBuilder::class.qualifiedName + "-tooltip"

typealias Content = @Composable () -> Unit

@Composable
fun rememberRichString(vararg keys: Any, builder: RichStringBuilder.() -> Unit) = remember(*keys) {
    RichStringBuilder().apply(builder).build()
}

@OptIn(ExperimentalTextApi::class)
open class RichStringBuilder {
    private val builder = AnnotatedString.Builder()
    private val inline = mutableMapOf<String, InlineTextContent>()
    private val clickables = mutableMapOf<String, () -> Unit>()
    private val tooltips = mutableMapOf<String, Tooltip>()

    fun append(text: String): RichStringBuilder {
        builder.append(text)
        return this
    }

    fun append(text: String, style: SpanStyle): RichStringBuilder {
        builder.append(text, style)
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

    @Composable
    fun inline(
        id: String,
        width: TextUnit,
        height: TextUnit = LocalTextStyle.current.fontSize,
        align: PlaceholderVerticalAlign = PlaceholderVerticalAlign.AboveBaseline,
        content: Content,
    ): RichStringBuilder {
        inline.putFirst(id, inlineText(width, height, align) { content() })
        builder.appendInlineContent(id)
        return this
    }

    fun withClickable(id: String, action: () -> Unit, content: RichStringBuilder.() -> Unit): RichStringBuilder {
        clickables.putFirst(id, action)
        builder.withAnnotation(CLICKABLE_TAG, id) {
            content()
        }
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
        builder.withAnnotation(TOOLTIP_TAG, id) {
            content()
        }
        return this
    }

    fun tooltipText(text: String, id: String, style: SpanStyle = SpanStyle(), tooltip: Tooltip) =
        withTooltip(id, tooltip) { append(text, style) }

    @Composable
    fun icon(
        icon: Painter,
        size: DpSize,
        id: String,
        modifier: Modifier = Modifier,
        description: String = id,
    ) = inline(id, size.width.dpToSp(), size.height.dpToSp(), PlaceholderVerticalAlign.Center) {
        Icon(icon, description, Modifier.size(size).then(modifier))
    }

    fun build() = RichString(builder.build(), inline, clickables, tooltips)
}

// FIXME convert back to default parameter once that doesn't crash the compiler
fun RichStringBuilder.link(text: String, uri: String, id: String) = link(text, uri, id, LINK_STYLE)
expect fun RichStringBuilder.link(text: String, uri: String, id: String, style: SpanStyle): RichStringBuilder
