package org.hertsig.compose.component.flow

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.hertsig.compose.component.ScrollableColumn
import org.hertsig.compose.util.Content

@Composable
fun ScrollableFlowColumn(
    columnSpacing: Dp = 0.dp,
    itemSpacing: Dp = 0.dp,
    strategy: FlowLayoutStrategy = VerticalThenHorizontalStrategy(),
    columns: Int = 2,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    content: Content
) {
    ScrollableColumn(modifier, state = state) {
        item {
            Layout(content) { measurables, outerConstraints ->
                val columnSpacingPx = columnSpacing.roundToPx()
                val itemSpacingPx = itemSpacing.roundToPx()

                val availableContentWidth = outerConstraints.maxWidth - spacingSize(columnSpacingPx, columns)
                val maxColumnWidth = availableContentWidth / columns
                val columnWidthConstraint = Constraints(maxWidth = maxColumnWidth)
                val placeables = measurables.map { it.measure(columnWidthConstraint) }
                val columnWidth = placeables.maxOfOrNull { it.width } ?: 0
                require(columnWidth <= maxColumnWidth) { "Doesn't fit: $columnWidth > $maxColumnWidth" }
                val finalLayout = strategy.layout(columns, itemSpacingPx, placeables)
                require(finalLayout.size == columns)

                layout(
                    outerConstraints.maxWidth,
                    finalLayout.maxOf { spacedSize(it.sumOf { p -> p.height }, itemSpacingPx, it.size) },
                ) {
                    var x = 0
                    finalLayout.forEach { placeables ->
                        var y = 0
                        placeables.forEach {
                            it.place(x, y)
                            y += it.height + itemSpacingPx
                        }
                        x += maxColumnWidth + columnSpacingPx
                    }
                }
            }
        }
    }
}

internal fun spacedSize(contentSize: Int, spacing: Int, items: Int, unspacedItems: Int = 1) =
    spacingSize(spacing, items, unspacedItems) + contentSize
private fun spacingSize(spacing: Int, size: Int, unspaced: Int = 1) =
    (size - unspaced).coerceAtLeast(0) * spacing
