package org.hertsig.compose.component

import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.hertsig.compose.Content

data class TabBuilder(
    val title: Content,
    val content: Content,
) {
    constructor(name: String, content: Content): this({ TextLine(name) }, content)
}

@Composable
fun TabView(
    rowHeight: Dp = 32.dp,
    indexState: MutableState<Int> = remember { mutableStateOf(0) },
    vararg views: TabBuilder,
) {
    var currentIndex by indexState
    CompositionLocalProvider(
        LocalTextStyle provides LocalTextStyle.current.copy(color = LocalContentColor.current)
    ) {
        TabRow(currentIndex, Modifier.height(rowHeight)) {
            views.forEachIndexed { index, it ->
                Tab(currentIndex == index, { currentIndex = index }) {
                    it.title()
                }
            }
        }
    }
    views.getOrNull(currentIndex)?.let { it.content() }
}
