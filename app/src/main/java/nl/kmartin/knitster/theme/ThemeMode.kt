package nl.kmartin.knitster.theme

import androidx.annotation.DrawableRes
import nl.kmartin.knitster.R

enum class ThemeMode(
    val id: String,
    val displayName: String,
    @param:DrawableRes val drawableRes: Int
) {
    SYSTEM("system_default", "System default", R.drawable.ic_dark_mode_system),
    LIGHT("light", "Light", R.drawable.ic_light_mode),
    DARK("dark", "Dark", R.drawable.ic_dark_mode);

    companion object {
        val DEFAULT = SYSTEM

        fun fromId(id: String): ThemeMode {
            return ThemeMode.entries.firstOrNull { it.id == id } ?: DEFAULT
        }
    }
}

fun ThemeMode.resolveDarkTheme(systemInDarkTheme: Boolean): Boolean = when (this) {
    ThemeMode.SYSTEM -> systemInDarkTheme
    ThemeMode.LIGHT -> false
    ThemeMode.DARK -> true
}