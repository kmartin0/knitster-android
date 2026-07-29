package nl.kmartin.knitster.data.repository


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import nl.kmartin.knitster.theme.ThemeColor
import nl.kmartin.knitster.theme.ThemeMode
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Persists and exposes the application's theme preferences.
 *
 * Theme preferences are stored using DataStore and exposed as observable
 * [Flow]s that emit the currently selected colour theme and brightness mode.
 */
@Singleton
class ThemeRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private object Keys {
        val THEME_COLOR = stringPreferencesKey("theme_color")
        val THEME_MODE = stringPreferencesKey("theme_mode")
    }

    /**
     * Observes the currently selected color theme.
     */
    val observeThemeColor: Flow<ThemeColor> = dataStore.data.map { prefs ->
        prefs[Keys.THEME_COLOR]?.let { ThemeColor.fromId(it) } ?: ThemeColor.DEFAULT
    }

    /**
     * Observes the currently selected brightness mode.
     */
    val observeThemeMode: Flow<ThemeMode> = dataStore.data.map { prefs ->
        prefs[Keys.THEME_MODE]?.let { ThemeMode.fromId(it) } ?: ThemeMode.DEFAULT
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