@file:OptIn(ExperimentalComposeUiApi::class)

package org.hertsig.compose

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*

typealias KeyEventHandler = (KeyEvent) -> Boolean

fun keyEvent(key: Key, type: KeyEventType = KeyEventType.KeyUp, action: () -> Unit): KeyEventHandler =
    keyEvent(listOf(key), type, action)

fun keyEvent(keys: List<Key>, type: KeyEventType = KeyEventType.KeyUp, action: () -> Unit): KeyEventHandler = {
    when {
        it.type != type -> false
        it.key in keys -> { action(); true }
        else -> false
    }
}

fun Modifier.onPlus(action: () -> Unit) = onKeyEvent(keyEvent(listOf(Key.Plus, Key.NumPadAdd), action = action))
fun Modifier.onMinus(action: () -> Unit) = onKeyEvent(keyEvent(listOf(Key.Minus, Key.NumPadSubtract), action = action))
fun Modifier.onEnter(action: () -> Unit) = onKeyEvent(keyEvent(listOf(Key.Enter, Key.NumPadEnter), action = action))
fun Modifier.onSpace(action: () -> Unit) = onKeyEvent(keyEvent(Key.Spacebar, action = action))
fun Modifier.onTab(action: () -> Unit) = onKeyEvent(keyEvent(Key.Tab, action = action))
