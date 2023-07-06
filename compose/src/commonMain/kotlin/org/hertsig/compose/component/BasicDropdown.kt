package org.hertsig.compose.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.hertsig.util.display

@Composable
fun <V> BasicDropdown(
    state: MutableState<V>,
    values: Collection<V>,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    itemAlign: TextAlign = TextAlign.Start,
    onUpdate: (V) -> Unit = {},
    display: (V) -> String = { it.toString() }
) {
    var value by state
    val showState = remember { mutableStateOf(false) }
    var show by showState
    Row {
        BasicTextField(display(value), {},
            modifier.border(1.dp, Color.Black).padding(2.dp).clickable { show = true },
            textStyle = TextStyle(textAlign = textAlign),
            // enabled = false is required to make clickable work (???)
            enabled = false, readOnly = true, singleLine = true, maxLines = 1)
        DropdownMenu(showState, values, display, itemAlign) { value = it; show = false; onUpdate(it) }
    }
}
