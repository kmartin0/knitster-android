package nl.kmartin.knitster.data.repository


import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import nl.kmartin.knitster.data.datasource.AppLocaleObserver
import nl.kmartin.knitster.data.model.AppSettings
import nl.kmartin.knitster.data.model.LanguageMode
import nl.kmartin.knitster.data.model.ThemeColor
import nl.kmartin.knitster.data.model.ThemeMode
import nl.kmartin.knitster.data.model.toLanguageMode
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Persists and exposes the application's settings.
 *
 * Theme preferences are stored using DataStore, while application locale changes
 * are observed through [AppLocaleObserver] and applied via [AppCompatDelegate].
 */
@Singleton
class SettingsRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val appLocaleObserver: AppLocaleObserver
) {
    private object Keys {
        val THEME_COLOR = stringPreferencesKey("theme_color")
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val KEEP_SCREEN_AWAKE = booleanPreferencesKey("keep_screen_awake")
    }

    /**
     * Observes the current application settings.
     *
     * Persisted preferences are read from DataStore and missing values fall back to the
     * defaults defined by [AppSettings]. The current language mode is derived from
     * [AppLocaleObserver].
     *
     * @return A flow that emits the current application settings whenever they change.
     */
    fun observeSettings(): Flow<AppSettings> {
        val defaults = AppSettings()

        return combine(dataStore.data, appLocaleObserver.appLocales) { prefs, locales ->
            AppSettings(
                themeColor = prefs[Keys.THEME_COLOR]?.let(ThemeColor::fromId)
                    ?: defaults.themeColor,
                themeMode = prefs[Keys.THEME_MODE]?.let(ThemeMode::fromId) ?: defaults.themeMode,
                keepScreenAwake = prefs[Keys.KEEP_SCREEN_AWAKE] ?: defaults.keepScreenAwake,
                appLocales = locales.toLanguageMode()
            )
        }
    }

    /**
     * Persists the selected color theme.
     *
     * @param themeColor color theme to persist.
     */
    suspend fun setThemeColor(themeColor: ThemeColor) {
        dataStore.edit { prefs -> prefs[Keys.THEME_COLOR] = themeColor.id }
    }

    /**
     * Persists the selected brightness mode.
     *
     * @param themeMode Brightness mode to persist.
     */
    suspend fun setThemeMode(themeMode: ThemeMode) {
        dataStore.edit { prefs -> prefs[Keys.THEME_MODE] = themeMode.id }
    }

    /**
     * Persists whether the screen should remain awake while the application is in use.
     *
     * @param keepScreenAwake Whether to prevent the screen from turning off.
     */
    suspend fun setKeepScreenAwake(keepScreenAwake: Boolean) {
        dataStore.edit { prefs -> prefs[Keys.KEEP_SCREEN_AWAKE] = keepScreenAwake }
    }

    /**
     * Converts the application locales provided by [AppCompatDelegate] to a [LanguageMode].
     *
     * @return The currently selected application language mode.
     */
    fun getCurrentLanguageMode(): LanguageMode {
        return AppCompatDelegate.getApplicationLocales().toLanguageMode()
    }

    /**
     * Applies the selected application language mode via [AppCompatDelegate].
     *
     * On Android versions below API 33, [AppLocaleObserver] is synchronized
     * explicitly after the locale change.
     *
     * @param languageMode Language mode to apply.
     */
    fun setLanguageMode(languageMode: LanguageMode) {
        if (getCurrentLanguageMode() == languageMode) return

        val localeList = languageMode.localeTag
            ?.let(LocaleListCompat::forLanguageTags)
            ?: LocaleListCompat.getEmptyLocaleList()

        AppCompatDelegate.setApplicationLocales(localeList)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            appLocaleObserver.syncAppLocales()
        }
    }
}