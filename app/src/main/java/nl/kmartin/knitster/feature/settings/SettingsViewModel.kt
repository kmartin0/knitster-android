package nl.kmartin.knitster.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nl.kmartin.knitster.R
import nl.kmartin.knitster.data.model.LanguageMode
import nl.kmartin.knitster.data.model.ThemeColor
import nl.kmartin.knitster.data.model.ThemeMode
import nl.kmartin.knitster.data.repository.SettingsRepository
import nl.kmartin.knitster.ui.UiText
import nl.kmartin.knitster.ui.launchOperation
import javax.inject.Inject

private const val TAG = "SettingsViewModel"

/**
 * Manages the state and user interactions for the settings screen.
 *
 * Observes the current application settings from [SettingsRepository] and
 * exposes them through [uiState]. Theme changes are persisted immediately,
 * while language changes are applied through the application's locale system.
 * Theme update failures are exposed through [SettingsUiState.settingsErrorMsg].
 *
 * @param settingsRepository Repository used to observe and update application settings.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        observeAppSettings()
    }

    /**
     * Persists the selected theme color.
     *
     * Any failure is exposed through [SettingsUiState.settingsErrorMsg].
     *
     * @param newThemeColor Theme color to persist.
     */
    fun setThemeColor(newThemeColor: ThemeColor) {
        launchOperation(
            operation = { settingsRepository.setThemeColor(newThemeColor) },
            onError = {
                _uiState.update {
                    it.copy(settingsErrorMsg = UiText.Resource(R.string.error_update_theme_colour))
                }
            },
            logTag = TAG,
            errorLogMsg = "Failed to save theme colour"
        )
    }

    /**
     * Persists the selected brightness mode.
     *
     * Any failure is exposed through [SettingsUiState.settingsErrorMsg].
     *
     * @param newThemeMode Brightness mode to persist.
     */
    fun setThemeMode(newThemeMode: ThemeMode) {
        launchOperation(
            operation = { settingsRepository.setThemeMode(newThemeMode) },
            onError = {
                _uiState.update {
                    it.copy(settingsErrorMsg = UiText.Resource(R.string.error_update_theme_mode))
                }
            },
            logTag = TAG,
            errorLogMsg = "Failed to save theme mode"
        )
    }

    /**
     * Applies the selected application language mode.
     *
     * @param newLanguageMode Language mode to apply.
     */
    fun setLanguageMode(newLanguageMode: LanguageMode) {
        settingsRepository.setLanguageMode(newLanguageMode)
    }

    /**
     * Persists whether the screen should remain awake while the application is in use.
     *
     * Any failure is exposed through [SettingsUiState.settingsErrorMsg].
     *
     * @param newKeepScreenAwake Whether to prevent the screen from turning off.
     */
    fun setKeepScreenAwake(newKeepScreenAwake: Boolean) {
        launchOperation(
            operation = { settingsRepository.setKeepScreenAwake(newKeepScreenAwake) },
            onError = {
                _uiState.update {
                    it.copy(settingsErrorMsg = UiText.Resource(R.string.error_update_keep_screen_awake))
                }
            },
            logTag = TAG,
            errorLogMsg = "Failed to save keep screen awake"
        )
    }

    /**
     * Clears the pending settings error message.
     */
    fun clearSettingsErrorMsg() {
        _uiState.update { it.copy(settingsErrorMsg = null) }
    }

    /**
     * Observes the current application settings and updates the UI state whenever
     * they change.
     */
    private fun observeAppSettings() {
        viewModelScope.launch {
            settingsRepository.observeSettings().collect { settings ->
                _uiState.update {
                    it.copy(settings = settings)
                }
            }
        }
    }
}