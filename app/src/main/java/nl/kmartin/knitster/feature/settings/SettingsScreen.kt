package nl.kmartin.knitster.feature.settings

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import nl.kmartin.knitster.ui.component.ObserveSnackbarError
import nl.kmartin.knitster.ui.toDisplayStringOrNull

/**
 * Displays the settings screen and connects it to the [SettingsViewModel].
 *
 * Observes the current application settings, handles settings-related errors,
 * and forwards user actions to the view model.
 *
 * @param viewModel ViewModel providing the settings state and handling updates.
 * @param onNavigateBack Called when the user navigates back from the settings screen.
 */
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveSnackbarError(
        errorMessage = uiState.settingsErrorMsg.toDisplayStringOrNull(),
        snackbarHostState = snackbarHostState,
        onErrorShown = viewModel::clearSettingsErrorMsg
    )

    SettingsScreenContent(
        snackbarHostState = snackbarHostState,
        settings = uiState.settings,
        onThemeColorSelected = viewModel::setThemeColor,
        onThemeModeSelected = viewModel::setThemeMode,
        onLanguageModeSelected = viewModel::setLanguageMode,
        onKeepScreenAwakeChanged = viewModel::setKeepScreenAwake,
        onBackClick = onNavigateBack
    )
}
