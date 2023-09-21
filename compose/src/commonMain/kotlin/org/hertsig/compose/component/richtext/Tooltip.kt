package org.hertsig.compose.component.richtext

import androidx.compose.ui.Modifier
import androidx.compose.ui.window.PopupPositionProvider
import org.hertsig.compose.util.Content
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

data class Tooltip(
    val modifier: Modifier = Modifier,
    val delay: Duration = 500.milliseconds,
    val positionProvider: PopupPositionProvider = TooltipPositionProvider,
    val content: Content,
)
