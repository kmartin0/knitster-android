package nl.kmartin.knitster.feature.settings

import nl.kmartin.knitster.data.model.LanguageMode
import nl.kmartin.knitster.data.model.ThemeColor
import nl.kmartin.knitster.data.model.ThemeMode

sealed interface SettingsIntent {
    data class ThemeColorSelected(val themeColor: ThemeColor) : SettingsIntent
    data class ThemeModeSelected(val themeMode: ThemeMode) : SettingsIntent
    data class LanguageModeSelected(val languageMode: LanguageMode) : SettingsIntent
    data class KeepScreenAwakeChanged(val keepScreenAwake: Boolean) : SettingsIntent
    data object SettingsErrorDismissed : SettingsIntent
}