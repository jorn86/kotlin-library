package org.hertsig.compose.component

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import org.hertsig.compose.util.Content

@Composable
fun Theme(
    lightColors: ColorScheme = lightColorScheme(),
    darkColors: ColorScheme = darkColorScheme(),
    typography: Typography = Typography(),
    shapes: Shapes = Shapes(),
    content: Content,
) {
    Theme(if (isSystemInDarkTheme()) darkColors else lightColors, typography, shapes, content)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Theme(
    colors: ColorScheme = lightColorScheme(),
    typography: Typography = Typography(),
    shapes: Shapes = Shapes(),
    content: Content,
) {
    MaterialTheme(colors, shapes, typography) {
        CompositionLocalProvider(
            LocalMinimumInteractiveComponentEnforcement provides false,
            // Why doesn't MaterialTheme do this automatically? It just keeps the default of Color.Black
            LocalContentColor provides MaterialTheme.colorScheme.onSurface,
        ) {
            content()
        }
    }
}
