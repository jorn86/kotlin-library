package org.hertsig.compose.component.flow

import androidx.compose.ui.layout.Placeable

interface FlowLayoutStrategy {
    fun layout(columns: Int, spacing: Int, placeables: List<Placeable>): List<List<Placeable>>
}

open class VerticalThenHorizontalStrategy: FlowLayoutStrategy {
    override fun layout(columns: Int, spacing: Int, placeables: List<Placeable>): List<List<Placeable>> {
        val averageColumnHeight = spacedSize(placeables.sumOf { it.height }, spacing, placeables.size, columns) / columns
        val layout = List(columns) { mutableListOf<Placeable>() }

        var currentColumnHeight = 0
        var column = 0
        placeables.forEach {
            layout[column].add(it)
            currentColumnHeight += it.height + spacing
            if (column < columns - 1 && shouldUseNextColumn(currentColumnHeight, averageColumnHeight)) {
                column++
                currentColumnHeight = 0
            }
        }
        return layout
    }

    protected open fun shouldUseNextColumn(currentColumnHeight: Int, averageColumnHeight: Int) =
        currentColumnHeight > averageColumnHeight
}

object HorizontalThenVerticalStrategy : FlowLayoutStrategy {
    override fun layout(columns: Int, spacing: Int, placeables: List<Placeable>): List<List<Placeable>> {
        val layout = List(columns) { mutableListOf<Placeable>() }
        placeables.forEachIndexed { index, it ->
            layout[index % layout.size].add(it)
        }
        return layout
    }
}

object ReorderStrategy: FlowLayoutStrategy {
    override fun layout(columns: Int, spacing: Int, placeables: List<Placeable>): List<List<Placeable>> {
        val layout = MutableList(columns) { mutableListOf<Placeable>() }
        placeables.sortedByDescending { it.height }.forEach { placeable ->
            layout.minBy { column -> spacedSize(column.sumOf { it.height }, spacing, column.size) }.add(placeable)
        }
        layout.forEach { column -> column.sortBy { placeables.indexOf(it) } }
        layout.sortBy { if (it.isEmpty()) placeables.size else placeables.indexOf(it.first()) }
        return layout
    }
}