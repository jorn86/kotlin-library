package org.hertsig.compose.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.material3.IconButton as MaterialIconButton

@Composable
fun IconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    buttonModifier: Modifier = Modifier,
    enabled: Boolean = true,
    iconSize: Dp = 24.dp,
    description: String = icon.name,
) {
    IconButton(onClick, rememberVectorPainter(icon), description, buttonModifier, enabled, iconSize)
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
    MaterialIconButton(onClick, buttonModifier.size(iconSize), enabled) { // TODO explicit size shouldn't be needed, see if there's a cleaner solution
        Icon(icon, description, Modifier.size(iconSize))
    }
}
