package org.hertsig.compose.component

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun Theme(
    lightColors: Colors = lightColors(),
    darkColors: Colors = darkColors(),
    typography: Typography = Typography(),
    shapes: Shapes = Shapes(),
    content: @Composable () -> Unit
) {
    Theme(if (isSystemInDarkTheme()) darkColors else lightColors, typography, shapes, content)
}

@Composable
fun Theme(
    colors: Colors = lightColors(),
    typography: Typography = Typography(),
    shapes: Shapes = Shapes(),
    content: @Composable () -> Unit
) {
    MaterialTheme(colors, typography, shapes) {
        CompositionLocalProvider(
            LocalContentColor provides colors.onSurface,
            LocalTextStyle provides LocalTextStyle.current.copy(color = colors.onSurface),
        ) {
            content()
        }
    }
}
