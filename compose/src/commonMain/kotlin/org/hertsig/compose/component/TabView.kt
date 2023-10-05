package org.hertsig.compose.component

import androidx.compose.foundation.layout.height
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.hertsig.compose.util.Content

data class TabBuilder(
    val title: Content,
    val content: Content,
) {
    constructor(name: String, content: Content): this({ TextLine(name) }, content)
}

@Composable
fun TabView(
    vararg views: TabBuilder,
    rowHeight: Dp = 32.dp,
    indexState: MutableState<Int> = remember { mutableStateOf(0) },
) = TabView(views.asList(), rowHeight, indexState)

@Composable
fun TabView(
    views: List<TabBuilder>,
    rowHeight: Dp = 40.dp,
    indexState: MutableState<Int> = remember { mutableStateOf(0) },
) {
    var currentIndex by indexState
    TabRow(currentIndex, Modifier.height(rowHeight)) {
        // TabRow changes the content color, revert that here
        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
            views.forEachIndexed { index, it ->
                Tab(currentIndex == index, { currentIndex = index }) { it.title() }
            }
        }
    }
    views.getOrNull(currentIndex)?.let { it.content() }
}
