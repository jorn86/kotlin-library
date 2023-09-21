package org.hertsig.compose.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

typealias Content = @Composable () -> Unit

@Composable
fun Modifier.autoFocus(requester: FocusRequester = remember { FocusRequester() }): Modifier {
    LaunchedEffect(Unit) { requester.requestFocus() }
    return focusRequester(requester)
}

fun Dp.dpToSp(density: Density) = (value * density.density / density.fontScale).sp

fun Modifier.pointerInputAsync(vararg keys: Any, action: suspend PointerInputScope.() -> Unit) = pointerInput(keys) {
    coroutineScope {
        launch {
            action()
        }
    }
}
