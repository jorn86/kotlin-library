package org.hertsig.compose.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerKeyboardModifiers
import androidx.compose.ui.input.pointer.isAltPressed
import androidx.compose.ui.input.pointer.isCtrlPressed
import androidx.compose.ui.input.pointer.isShiftPressed
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.hertsig.compose.onMinus
import org.hertsig.compose.onPlus

@Composable
fun BasicEditNumber(
    state: MutableState<Int>,
    min: Int = 0,
    max: Int,
    step: Int = 1,
    width: Dp = 20.dp,
    suffix: String? = null,
    showButtons: Boolean = true,
    onUpdate: (Int) -> Unit = {},
) {
    var value by state
    var display by remember(value) { mutableStateOf(TextFieldValue(value.toString())) }
    LaunchedEffect(value) { display = TextFieldValue(value.toString(), TextRange(9)) }
    fun set(new: Int) {
        value = new.coerceIn(min, max)
        onUpdate(value)
    }

    val keys = LocalWindowInfo.current.keyboardModifiers
    val minus = { set(value - step(step, keys)) }
    val plus = { set(value + step(step, keys)) }

    Row(verticalAlignment = Alignment.CenterVertically) {
        BasicTextField(
            display,
            { it.text.toIntOrNull()?.let(::set) },
            Modifier.onFocusChanged { if (it.isFocused) display = display.copy(selection = TextRange(0, 9)) }
                .onPlus(plus)
                .onMinus(minus)
                .border(1.dp, Color.Black).padding(2.dp).width(width),
            singleLine = true,
            maxLines = 1
        ) { innerTextField ->
            innerTextField()
            if (suffix != null) TextLine(suffix, style = TextStyle.Default, align = TextAlign.End)
        }
        if (showButtons) {
            val noFocus = Modifier.focusProperties { canFocus = false }
            IconButton(minus, Icons.Default.Remove, noFocus, value > min, 12.dp, "Decrease")
            IconButton(plus, Icons.Default.Add, noFocus, value < max, 12.dp, "Increase")
        }
    }
}

private fun step(step: Int, keys: PointerKeyboardModifiers): Int {
    var total = step
    if (keys.isShiftPressed) total *= 2
    if (keys.isCtrlPressed) total *= 5
    if (keys.isAltPressed) total *= 10
    return total
}
