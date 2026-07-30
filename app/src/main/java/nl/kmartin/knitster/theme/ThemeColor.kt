package nl.kmartin.knitster.theme

enum class ThemeColor(
    val id: String,
    val displayName: String
) {
    CREAM("cream", "Cream"),
    GREEN("green", "Green"),
    BLUE("blue", "Blue"),
    PINK("pink", "Pink");

    companion object {
        val DEFAULT = CREAM

        fun fromId(id: String): ThemeColor {
            return ThemeColor.entries.firstOrNull { it.id == id } ?: DEFAULT
        }
    }
}