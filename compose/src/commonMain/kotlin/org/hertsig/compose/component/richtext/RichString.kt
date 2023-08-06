package org.hertsig.compose.component.richtext

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.ui.text.AnnotatedString

open class RichString internal constructor(
    val annotatedString: AnnotatedString,
    val inlineContent: Map<String, InlineTextContent>,
    val clickables: Map<String, () -> Unit>,
    val tooltips: Map<String, Tooltip>,
)
