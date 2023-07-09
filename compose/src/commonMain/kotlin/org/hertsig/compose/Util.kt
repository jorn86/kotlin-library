package org.hertsig.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle

@Composable
fun Modifier.autoFocus(requester: FocusRequester = remember { FocusRequester() }): Modifier {
    LaunchedEffect(Unit) { requester.requestFocus() }
    return focusRequester(requester)
}

fun AnnotatedString.Builder.append(style: SpanStyle, text: String): AnnotatedString.Builder {
    withStyle(style) { append(text) }
    return this
}

fun AnnotatedString.Builder.appendText(text: String): AnnotatedString.Builder {
    append(text)
    return this
}

fun AnnotatedString.Builder.build() = toAnnotatedString()
