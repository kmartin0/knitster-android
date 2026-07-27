package nl.kmartin.knitster.data.model

import androidx.annotation.DrawableRes
import nl.kmartin.knitster.R

enum class ProjectIcon(
    val id: String,
    @param:DrawableRes val drawableRes: Int
) {
    SWEATER("sweater", R.drawable.ic_sweater),
    CHEVRON("chevron", R.drawable.ic_chevron_forward),
    ARROW_BACK("arrow_back", R.drawable.ic_arrow_back),
    ADD("add", R.drawable.ic_add),
    REMOVE("remove", R.drawable.ic_remove),
    MORE_VERT("more_vert", R.drawable.ic_more_vert),;

    companion object {
        val DEFAULT = SWEATER

        fun fromId(id: String): ProjectIcon {
            return entries.firstOrNull { it.id == id } ?: DEFAULT
        }
    }
}