package org.hertsig.compose.component

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import org.hertsig.compose.Content

@Composable
fun Theme(
    lightColors: Colors = lightColors(),
    darkColors: Colors = darkColors(),
    typography: Typography = Typography(),
    shapes: Shapes = Shapes(),
    content: Content
) {
    Theme(if (isSystemInDarkTheme()) darkColors else lightColors, typography, shapes, content)
}

@Composable
fun Theme(
    colors: Colors = lightColors(),
    typography: Typography = Typography(),
    shapes: Shapes = Shapes(),
    content: Content
) {
    MaterialTheme(colors, typography, shapes) {
        CompositionLocalProvider(LocalContentColor provides colors.onSurface) {
            content()
        }
    }
}
