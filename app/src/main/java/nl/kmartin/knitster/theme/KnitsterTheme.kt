package nl.kmartin.knitster.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import nl.kmartin.knitster.data.model.ThemeColor
import nl.kmartin.knitster.data.model.ThemeMode
import nl.kmartin.knitster.data.model.resolveDarkTheme
import nl.kmartin.knitster.theme.colorscheme.BlueDarkScheme
import nl.kmartin.knitster.theme.colorscheme.BlueLightScheme
import nl.kmartin.knitster.theme.colorscheme.CreamDarkScheme
import nl.kmartin.knitster.theme.colorscheme.CreamLightScheme
import nl.kmartin.knitster.theme.colorscheme.GreenDarkScheme
import nl.kmartin.knitster.theme.colorscheme.GreenLightScheme
import nl.kmartin.knitster.theme.colorscheme.PinkDarkScheme
import nl.kmartin.knitster.theme.colorscheme.PinkLightScheme

@Composable
fun KnitsterTheme(
    themeColor: ThemeColor = ThemeColor.DEFAULT,
    themeMode: ThemeMode = ThemeMode.DEFAULT,
    content: @Composable () -> Unit
) {
    val isDarkMode = themeMode.resolveDarkTheme(isSystemInDarkTheme())
    val colorScheme = colorSchemeFor(themeColor, isDarkMode)

    MaterialTheme(
        colorScheme = colorScheme,
        typography = KnitsterTypography,
        shapes = KnitsterShapes,
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            content()
        }
    }
}

fun colorSchemeFor(themeColor: ThemeColor, isDarkMode: Boolean): ColorScheme {
    return when (themeColor) {
        ThemeColor.CREAM -> if (isDarkMode) CreamDarkScheme else CreamLightScheme
        ThemeColor.GREEN -> if (isDarkMode) GreenDarkScheme else GreenLightScheme
        ThemeColor.BLUE -> if (isDarkMode) BlueDarkScheme else BlueLightScheme
        ThemeColor.PINK -> if (isDarkMode) PinkDarkScheme else PinkLightScheme
    }
}
