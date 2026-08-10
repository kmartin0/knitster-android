package nl.kmartin.knitster.data.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import nl.kmartin.knitster.R

enum class ThemeMode(
    val id: String,
    @param:StringRes val displayNameRes: Int,
    @param:DrawableRes val drawableRes: Int
) {
    SYSTEM(
        id = "system_default",
        displayNameRes = R.string.theme_mode_system_default,
        drawableRes = R.drawable.ic_brightness_auto
    ),
    LIGHT(
        id = "light",
        displayNameRes = R.string.theme_mode_light,
        drawableRes = R.drawable.ic_brightness_light
    ),
    DARK(
        id = "dark",
        displayNameRes = R.string.theme_mode_dark,
        drawableRes = R.drawable.ic_brightness_dark
    );

    companion object {
        val DEFAULT = SYSTEM

        fun fromId(id: String): ThemeMode {
            return entries.firstOrNull { it.id == id } ?: DEFAULT
        }
    }
}

fun ThemeMode.resolveDarkTheme(systemInDarkTheme: Boolean): Boolean = when (this) {
    ThemeMode.SYSTEM -> systemInDarkTheme
    ThemeMode.LIGHT -> false
    ThemeMode.DARK -> true
}