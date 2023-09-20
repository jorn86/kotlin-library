package org.hertsig.compose.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.hertsig.compose.Content

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TooltipText(
    text: String,
    contentModifier: Modifier = Modifier,
    tooltipModifier: Modifier = tooltipModifier(),
    content: Content
) {
    TooltipArea({ Text(text, tooltipModifier) }, contentModifier, content = content)
}

@Composable
fun tooltipModifier(
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    borderColor: Color = MaterialTheme.colorScheme.onBackground,
    padding: Dp = 3.dp,
) = Modifier.background(backgroundColor).border(1.dp, borderColor).padding(padding)
