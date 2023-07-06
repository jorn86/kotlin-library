package org.hertsig.compose.component

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

const val dividerAlpha = .12f

@Composable
fun defaultColor(alpha: Float = dividerAlpha) = MaterialTheme.colors.onSurface.copy(alpha = alpha)

@Composable
fun HorizontalDivider(color: Color = defaultColor(), thickness: Dp = 1.dp) {
    Divider(Modifier.fillMaxWidth().height(thickness), color)
}

@Composable
fun VerticalDivider(color: Color = defaultColor(), thickness: Dp = 1.dp) {
    Divider(Modifier.fillMaxHeight().width(thickness), color)
}
