package org.hertsig.compose.component

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

const val dividerAlpha = .12f

@Composable
fun defaultColor(alpha: Float = dividerAlpha) = MaterialTheme.colorScheme.onSurface.copy(alpha = alpha)

@Composable
fun HorizontalDivider(color: Color = defaultColor(), thickness: Dp = 1.dp) {
    Divider(Modifier.fillMaxWidth().height(thickness), color = color)
}

@Composable
fun VerticalDivider(color: Color = defaultColor(), thickness: Dp = 1.dp) {
    Divider(Modifier.fillMaxHeight().width(thickness), color = color)
}
