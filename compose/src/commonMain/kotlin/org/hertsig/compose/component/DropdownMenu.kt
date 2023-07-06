package org.hertsig.compose.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextAlign

@Composable
fun <V> DropdownMenu(
    showState: MutableState<Boolean>,
    values: Collection<V>,
    display: (V) -> String = { it.toString() },
    itemAlign: TextAlign = TextAlign.Start,
    onUpdate: (V) -> Unit
) {
    var show by showState
    DropdownMenu(show, values, { show = false }, display, itemAlign, onUpdate)
}

@Composable
fun <V> DropdownMenu(
    visible: Boolean,
    values: Collection<V>,
    onDismiss: () -> Unit = {},
    display: (V) -> String = { it.toString() },
    itemAlign: TextAlign = TextAlign.Start,
    onUpdate: (V) -> Unit
) {
    androidx.compose.material3.DropdownMenu(visible, onDismiss) {
        values.forEach { Item(display(it), itemAlign) { onUpdate(it) } }
    }
}
