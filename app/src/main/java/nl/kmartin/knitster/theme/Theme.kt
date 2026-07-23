package nl.kmartin.knitster.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = KnitsterRust,
    onPrimary = KnitsterOnRust,
    primaryContainer = KnitsterRustContainer,
    onPrimaryContainer = KnitsterOnRustContainer,
    secondary = KnitsterSage,
    onSecondary = KnitsterOnSage,
    background = KnitsterCream,
    onBackground = KnitsterOnCream,
    surface = KnitsterSurface,
    onSurface = KnitsterOnSurface,
)

@Composable
fun KnitsterTheme(
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        dynamicLightColorScheme(LocalContext.current)
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = KnitsterShapes,
        content = content
    )
}