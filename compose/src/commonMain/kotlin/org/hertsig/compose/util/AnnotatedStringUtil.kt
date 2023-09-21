package org.hertsig.compose.util

import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle

@Deprecated("use rememberRichString instead")
fun AnnotatedString.Builder.append(text: String, style: SpanStyle): AnnotatedString.Builder {
    withStyle(style) { append(text) }
    return this
}

@Deprecated("use rememberRichString instead")
fun AnnotatedString.Builder.appendText(text: String): AnnotatedString.Builder {
    append(text)
    return this
}

@Deprecated("use rememberRichString instead")
fun AnnotatedString.Builder.inline(id: String, text: String = "\uFFFD"): AnnotatedString.Builder {
    appendInlineContent(id, text)
    return this
}

fun AnnotatedString.Builder.build() = toAnnotatedString()
