package nl.kmartin.knitster.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import nl.kmartin.knitster.R
import nl.kmartin.knitster.data.model.AppSettings
import nl.kmartin.knitster.data.model.LanguageMode
import nl.kmartin.knitster.data.model.ThemeColor
import nl.kmartin.knitster.data.model.ThemeMode
import nl.kmartin.knitster.theme.Dimensions
import nl.kmartin.knitster.theme.knitsterTopAppBarColors
import nl.kmartin.knitster.ui.component.AppBarCircularProgressIndicator
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

/**
 * Displays the content of the settings screen.
 *
 * The top app bar remains available while settings are loading. Settings controls
 * are displayed once [settings] becomes available.
 *
 * @param modifier Modifier to be applied to the root layout.
 * @param snackbarHostState State used to display snackbar messages.
 * @param settings Current application settings, or `null` while they are loading.
 * @param onThemeColorSelected Called when a theme color is selected.
 * @param onThemeModeSelected Called when a brightness mode is selected.
 * @param onLanguageModeSelected Called when an application language mode is selected.
 * @param onKeepScreenAwakeChanged Called when the `keepScreenAwake` setting is changed.
 * @param onBackClick Called when the back button is clicked.
 */
@Composable
private fun SettingsScreenContent(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    settings: AppSettings?,
    onThemeColorSelected: (ThemeColor) -> Unit,
    onThemeModeSelected: (ThemeMode) -> Unit,
    onLanguageModeSelected: (LanguageMode) -> Unit,
    onKeepScreenAwakeChanged: (Boolean) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            SettingsScreenTopAppBar(
                onBackClick = onBackClick,
                isLoading = settings == null
            )
        }
    ) { innerPadding ->
        settings?.let { loadedSettings ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(Dimensions.ScreenPadding)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                ThemeColorPicker(
                    currentThemeColor = loadedSettings.themeColor,
                    currentThemeMode = loadedSettings.themeMode,
                    onThemeColorSelected = onThemeColorSelected
                )

                ThemeModePicker(
                    currentThemeMode = loadedSettings.themeMode,
                    onThemeModeSelected = onThemeModeSelected
                )

                LanguagePicker(
                    currentLanguageMode = loadedSettings.appLocales,
                    onLanguageModeSelected = onLanguageModeSelected
                )

                GeneralSettings(
                    keepScreenAwake = loadedSettings.keepScreenAwake,
                    onKeepScreenAwakeChanged = onKeepScreenAwakeChanged
                )
            }
        }
    }
}

/**
 * Displays the settings screen top app bar.
 *
 * Shows a loading indicator while the application settings are being loaded.
 *
 * @param onBackClick Called when the back button is clicked.
 * @param isLoading Whether to display a loading indicator.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreenTopAppBar(
    onBackClick: () -> Unit,
    isLoading: Boolean
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.settings)
            )
        },
        colors = knitsterTopAppBarColors(),
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = stringResource(R.string.back),
                )
            }
        },
        actions = {
            if (isLoading) AppBarCircularProgressIndicator()
        }
    )
}

// --- Preview ---
@Preview(showBackground = true)
@Composable
private fun SettingsScreenContentPreview() {
    SettingsScreenContent(
        snackbarHostState = remember { SnackbarHostState() },
        settings = AppSettings(),
        onThemeColorSelected = {},
        onThemeModeSelected = {},
        onLanguageModeSelected = {},
        onKeepScreenAwakeChanged = {},
        onBackClick = {}
    )
}