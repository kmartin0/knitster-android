package nl.kmartin.knitster.data.model

import androidx.annotation.DrawableRes
import nl.kmartin.knitster.R

enum class ProjectIcon(
    val id: String,
    @param:DrawableRes val drawableRes: Int
) {
    KNITTING("knitting", R.drawable.ic_knitting_sketch),
    SWEATER("sweater", R.drawable.ic_sweater_sketch),
    DRESS("dress", R.drawable.ic_dress_sketch),
    PANTS("pants", R.drawable.ic_pants_sketch),
    SOCKS("socks", R.drawable.ic_socks_sketch),
    BEANIE("beanie", R.drawable.ic_beanie_sketch);

    companion object {
        val DEFAULT = KNITTING

        fun fromId(id: String): ProjectIcon {
            return entries.firstOrNull { it.id == id } ?: DEFAULT
        }
    }
}