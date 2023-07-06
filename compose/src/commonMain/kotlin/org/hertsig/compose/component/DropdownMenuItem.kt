package org.hertsig.compose.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
internal fun Item(text: String, align: TextAlign = TextAlign.Start, onClick: () -> Unit) {
    TextLine(text, Modifier.padding(2.dp).defaultMinSize(40.dp).fillMaxWidth().clickable { onClick() }, align = align)
}
