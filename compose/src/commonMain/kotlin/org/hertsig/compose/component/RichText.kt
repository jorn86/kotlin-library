package org.hertsig.compose.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import com.google.accompanist.flowlayout.FlowRow
import java.util.regex.Pattern

private val SPACES = Pattern.compile("\\s+")

@Composable
fun rememberRichText(vararg keys: Any, builder: RichTextParagraphScope.() -> Unit): RichTextString {
    return remember(keys) { RichTextParagraphScope().from(builder) }
}

@Composable
fun RichText(text: RichTextString, modifier: Modifier = Modifier, paragraphModifier: Modifier = Modifier) {
    Column(modifier) {
        text.paragraphs.forEach {
            FlowRow(paragraphModifier) {
                it.tokens.forEach { token -> token.render() }
            }
        }
    }
}

class RichTextParagraphScope internal constructor() {
    private val paragraphs = mutableListOf<RichTextParagraph>()

    internal fun from(builder: RichTextParagraphScope.() -> Unit): RichTextString {
        builder()
        return RichTextString(paragraphs)
    }

    fun paragraph(builder: RichTextScope.() -> Unit) {
        paragraphs.add(RichTextScope().from(builder))
    }
}

class RichTextScope internal constructor() {
    private val tokens = mutableListOf<RichTextToken>()

    internal fun from(builder: RichTextScope.() -> Unit): RichTextParagraph {
        builder()
        return RichTextParagraph(tokens)
    }

    fun breakingText(text: String, modifier: Modifier = Modifier, style: TextStyle? = null) {
        tokens.add(RichTextBreakingTextToken(text, modifier, style))
    }

    fun text(text: String, modifier: Modifier = Modifier, style: TextStyle? = null) {
        tokens.add(RichTextStringToken(text, modifier, style))
    }

    fun text(text: AnnotatedString, modifier: Modifier = Modifier, style: TextStyle? = null) {
        tokens.add(RichTextAnnotatedStringToken(text, modifier, style))
    }

    fun icon(icon: ImageVector, description: String, modifier: Modifier = Modifier, tint: Color = Color.Unspecified) {
        tokens.add(RichTextIconToken(icon, description, modifier, tint))
    }
}

class RichTextString(internal val paragraphs: List<RichTextParagraph>)
data class RichTextParagraph(val tokens: List<RichTextToken>)
sealed interface RichTextToken { @Composable fun render() }

private class RichTextIconToken(
    private val icon: ImageVector,
    private val description: String,
    private val modifier: Modifier,
    private val tint: Color,
): RichTextToken {
    @Composable
    override fun render() {
        val color = if (tint == Color.Unspecified) LocalContentColor.current.copy(alpha = LocalContentAlpha.current) else tint
        Icon(icon, description, modifier, color)
    }
}

private class RichTextBreakingTextToken(
    private val text: String,
    private val modifier: Modifier,
    private val style: TextStyle?,
): RichTextToken {
    @Composable
    override fun render() {
        val words = text.split(SPACES)
        words.forEachIndexed { index, it ->
            TextLine(it, modifier, style = style ?: LocalTextStyle.current)
            if (index != words.lastIndex) TextLine(" ")
        }
    }
}

private class RichTextStringToken(
    private val text: String,
    private val modifier: Modifier,
    private val style: TextStyle?,
): RichTextToken {
    @Composable
    override fun render() {
        TextLine(text, modifier, style = style ?: LocalTextStyle.current)
    }
}

private class RichTextAnnotatedStringToken(
    private val text: AnnotatedString,
    private val modifier: Modifier,
    private val style: TextStyle?,
): RichTextToken {
    @Composable
    override fun render() {
        TextLine(text, modifier, style = style ?: LocalTextStyle.current)
    }
}
