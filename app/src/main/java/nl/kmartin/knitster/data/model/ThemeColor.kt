package nl.kmartin.knitster.data.model

import androidx.annotation.StringRes
import nl.kmartin.knitster.R

enum class ThemeColor(
    val id: String,
    @param:StringRes val displayNameRes: Int
) {
    CREAM("cream", R.string.theme_color_cream),
    GREEN("green", R.string.theme_color_green),
    BLUE("blue", R.string.theme_color_blue),
    PINK("pink", R.string.theme_color_pink);

    companion object {
        val DEFAULT = CREAM

        fun fromId(id: String): ThemeColor {
            return entries.firstOrNull { it.id == id } ?: DEFAULT
        }
    }
}