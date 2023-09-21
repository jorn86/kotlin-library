package org.hertsig.compose.component.richtext

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.ui.text.AnnotatedString

open class RichString internal constructor(
    internal val annotatedString: AnnotatedString,
    internal val inlineContent: Map<String, InlineTextContent>,
    internal val clickables: Map<String, () -> Unit>,
    internal val tooltips: Map<String, Tooltip>,
) {
    override fun toString() = "RichString[$annotatedString]"
}
