package org.hertsig.compose.component

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SpacedRow(
    modifier: Modifier = Modifier,
    spacing: Dp = 4.dp,
    horizontal: Alignment.Horizontal = Alignment.Start,
    vertical: Alignment.Vertical = Alignment.Top,
    content: @Composable (RowScope.() -> Unit),
) {
    Row(modifier, Arrangement.spacedBy(spacing, horizontal), vertical, content)
}

@Composable
fun SpacedColumn(
    modifier: Modifier = Modifier,
    spacing: Dp = 4.dp,
    vertical: Alignment.Vertical = Alignment.Top,
    horizontal: Alignment.Horizontal = Alignment.Start,
    content: @Composable (ColumnScope.() -> Unit),
) {
    Column(modifier, Arrangement.spacedBy(spacing, vertical), horizontal, content)
}
