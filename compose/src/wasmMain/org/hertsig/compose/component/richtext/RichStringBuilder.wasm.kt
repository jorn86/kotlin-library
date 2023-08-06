package org.hertsig.compose.component.richtext

// let's see if this compiles when we actually enable this source set again

@JsFun("(uri) => { window.open(uri, '_blank') }")
private external fun open(uri: String)

actual fun RichStringBuilder.link(text: String, uri: String, id: String, style: SpanStyle): RichStringBuilder {
    return clickableText(text, id, style) { open(uri) }
}
