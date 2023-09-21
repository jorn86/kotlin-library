package org.hertsig.compose.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import org.hertsig.compose.util.Content

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IconWithTooltip(
    icon: ImageVector,
    description: String = icon.name,
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    tooltip: Content,
) {
    TooltipArea(tooltip, modifier) {
        Icon(icon, description, iconModifier)
    }
}
