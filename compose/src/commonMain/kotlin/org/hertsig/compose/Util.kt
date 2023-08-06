package org.hertsig.compose

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun Modifier.autoFocus(requester: FocusRequester = remember { FocusRequester() }): Modifier {
    LaunchedEffect(Unit) { requester.requestFocus() }
    return focusRequester(requester)
}

fun AnnotatedString.Builder.append(text: String, style: SpanStyle): AnnotatedString.Builder {
    withStyle(style) { append(text) }
    return this
}

fun AnnotatedString.Builder.appendText(text: String): AnnotatedString.Builder {
    append(text)
    return this
}

fun AnnotatedString.Builder.inline(id: String, text: String = "\uFFFD"): AnnotatedString.Builder {
    appendInlineContent(id, text)
    return this
}

fun AnnotatedString.Builder.build() = toAnnotatedString()

@Composable
fun inlineText(
    width: TextUnit,
    height: TextUnit = LocalTextStyle.current.fontSize,
    align: PlaceholderVerticalAlign = PlaceholderVerticalAlign.AboveBaseline,
    content: @Composable (String) -> Unit,
) = InlineTextContent(Placeholder(width, height, align), content)

@Composable
fun Dp.dpToSp(): TextUnit {
    return (value * LocalDensity.current.density / LocalDensity.current.fontScale).sp
}
