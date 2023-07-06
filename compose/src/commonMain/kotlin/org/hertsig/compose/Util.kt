package org.hertsig.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester

@Composable
fun Modifier.autoFocus(requester: FocusRequester = remember { FocusRequester() }): Modifier {
    LaunchedEffect(Unit) { requester.requestFocus() }
    return focusRequester(requester)
}
