package nl.kmartin.knitster.data.model

import androidx.annotation.StringRes
import androidx.core.os.LocaleListCompat
import nl.kmartin.knitster.R

/**
 * Represents the language modes available to the application.
 *
 * @param displayNameRes String resource used to display the language mode.
 * @param localeTag Language tag used for the application locale, or `null` to follow the system language.
 */
enum class LanguageMode(
    @param:StringRes val displayNameRes: Int,
    val localeTag: String?
) {
    SYSTEM(R.string.language_mode_system_default, null),
    ENGLISH(R.string.language_mode_english, "en"),
    DUTCH(R.string.language_mode_dutch, "nl");

    companion object {
        val DEFAULT = SYSTEM

        fun fromLocaleTag(localeTag: String?): LanguageMode {
            if (localeTag == null) return SYSTEM

            return entries.firstOrNull {
                it.localeTag == localeTag
            } ?: DEFAULT
        }
    }
}

/**
 * Converts [LocaleListCompat] to a [LanguageMode].
 *
 * An empty locale list represents the system language.
 *
 * @return Language mode matching the first application locale.
 */
fun LocaleListCompat.toLanguageMode(): LanguageMode {
    if (isEmpty) return LanguageMode.SYSTEM

    return LanguageMode.fromLocaleTag(
        this[0]?.language
    )
}
