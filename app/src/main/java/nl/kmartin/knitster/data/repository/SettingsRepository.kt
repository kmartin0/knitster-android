package nl.kmartin.knitster.data.repository


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import nl.kmartin.knitster.data.model.AppSettings
import nl.kmartin.knitster.theme.ThemeColor
import nl.kmartin.knitster.theme.ThemeMode
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Persists and exposes the application's settings preferences.
 *
 * settings preferences are stored using DataStore and exposed as observable [Flow]
 */
@Singleton
class SettingsRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private object Keys {
        val THEME_COLOR = stringPreferencesKey("theme_color")
        val THEME_MODE = stringPreferencesKey("theme_mode")
    }

    /**
     * Observes the persisted application settings.
     * Missing preferences fall back to the defaults defined by [AppSettings].
     *
     * @return A flow that emits the current application settings whenever they change.
     */
    fun observeSettings(): Flow<AppSettings> {
        val defaults = AppSettings()

        return dataStore.data.map { prefs ->
            AppSettings(
                themeColor = prefs[Keys.THEME_COLOR]
                    ?.let(ThemeColor::fromId)
                    ?: defaults.themeColor,
                themeMode = prefs[Keys.THEME_MODE]
                    ?.let(ThemeMode::fromId)
                    ?: defaults.themeMode
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
}