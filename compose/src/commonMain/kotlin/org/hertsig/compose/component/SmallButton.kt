package org.hertsig.compose.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SmallButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    padding: PaddingValues = PaddingValues(8.dp, 4.dp),
    content: @Composable RowScope.() -> Unit,
) {
    Button(onClick, modifier.heightIn(1.dp), enabled, colors = colors, contentPadding = padding, content = content)
}
