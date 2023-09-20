package org.hertsig.compose.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton as MaterialIconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun IconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    buttonModifier: Modifier = Modifier,
    enabled: Boolean = true,
    iconSize: Dp = 24.dp,
    description: String = icon.name,
) {
    MaterialIconButton(onClick, buttonModifier, enabled) {
        Icon(icon, description, Modifier.size(iconSize))
    }
}

@Composable
fun IconButton(
    onClick: () -> Unit,
    icon: Painter,
    description: String,
    buttonModifier: Modifier = Modifier,
    enabled: Boolean = true,
    iconSize: Dp = 24.dp,
) {
    MaterialIconButton(onClick, buttonModifier, enabled) {
        Icon(icon, description, Modifier.size(iconSize))
    }
}
