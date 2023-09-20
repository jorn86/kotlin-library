package org.hertsig.compose.component

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import org.hertsig.compose.Content

@Composable
fun Theme(
    lightColors: ColorScheme = lightColorScheme(),
    darkColors: ColorScheme = darkColorScheme(),
    typography: Typography = Typography(),
    shapes: Shapes = Shapes(),
    content: Content
) {
    Theme(if (isSystemInDarkTheme()) darkColors else lightColors, typography, shapes, content)
}

@Composable
fun Theme(
    colors: ColorScheme = lightColorScheme(),
    typography: Typography = Typography(),
    shapes: Shapes = Shapes(),
    content: Content
) {
    MaterialTheme(colors, shapes, typography) {
        content()
//        CompositionLocalProvider(LocalContentColor provides colors.onSurface) {
//        }
    }
}
