package nl.kmartin.knitster.feature.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nl.kmartin.knitster.R
import nl.kmartin.knitster.data.repository.SettingsRepository
import nl.kmartin.knitster.theme.ThemeColor
import nl.kmartin.knitster.theme.ThemeMode
import nl.kmartin.knitster.ui.UiText
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

private const val TAG = "SettingsViewModel"

/**
 * Manages the state and user interactions for the settings screen.
 *
 * Observes the persisted application settings from [SettingsRepository] and
 * exposes them through [uiState]. Theme changes are persisted immediately,
 * while update failures are exposed through [SettingsUiState.settingsErrorMsg].
 *
 * @param settingsRepository Repository used to observe and persist application settings.
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
        viewModelScope.launch {
            try {
                settingsRepository.setThemeColor(newThemeColor)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Failed to save theme colour", e)

                _uiState.update {
                    it.copy(settingsErrorMsg = UiText.Resource(R.string.error_update_theme_colour))
                }
            }
        }
    }

    /**
     * Persists the selected brightness mode.
     *
     * Any failure is exposed through [SettingsUiState.settingsErrorMsg].
     *
     * @param newThemeMode Brightness mode to persist.
     */
    fun setThemeMode(newThemeMode: ThemeMode) {
        viewModelScope.launch {
            try {
                settingsRepository.setThemeMode(newThemeMode)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Failed to save theme mode", e)

                _uiState.update {
                    it.copy(settingsErrorMsg = UiText.Resource(R.string.error_update_theme_mode))
                }
            }
        }
    }

    /**
     * Clears the pending settings error message.
     */
    fun clearSettingsErrorMsg() {
        _uiState.update { it.copy(settingsErrorMsg = null) }
    }

    /**
     * Observes persisted application settings and updates the UI state whenever
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