package org.hertsig.compose.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import org.hertsig.util.display

@Composable
inline fun <reified E: Enum<E>> BasicDropdown(
    state: MutableState<E>,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    itemAlign: TextAlign = TextAlign.Start,
    noinline onUpdate: (E) -> Unit = {},
    noinline display: (E) -> String = { it.display }
) {
    BasicDropdown(state, enumValues<E>().asList(), modifier, textAlign, itemAlign, onUpdate, display)
}
