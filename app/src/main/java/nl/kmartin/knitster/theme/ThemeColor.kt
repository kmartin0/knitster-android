package nl.kmartin.knitster.theme

enum class ThemeColor(
    val id: String,
    val displayName: String
) {
    CREAM("cream", "Cream"),
    GREEN("green", "Green"),
    LAVENDER("lavender", "Lavender"),
    RUST("rust", "Rust");

    companion object {
        val DEFAULT = CREAM

        fun fromId(id: String): ThemeColor {
            return ThemeColor.entries.firstOrNull { it.id == id } ?: DEFAULT
        }
    }
}