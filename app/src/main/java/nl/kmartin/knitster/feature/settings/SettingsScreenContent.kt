package nl.kmartin.knitster.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.kmartin.knitster.data.model.AppSettings
import nl.kmartin.knitster.data.model.LanguageMode
import nl.kmartin.knitster.data.model.ThemeColor
import nl.kmartin.knitster.data.model.ThemeMode
import nl.kmartin.knitster.feature.settings.component.GeneralSettings
import nl.kmartin.knitster.feature.settings.component.LanguagePicker
import nl.kmartin.knitster.feature.settings.component.SettingsScreenTopAppBar
import nl.kmartin.knitster.feature.settings.component.ThemeColorPicker
import nl.kmartin.knitster.feature.settings.component.ThemeModePicker
import nl.kmartin.knitster.theme.KnitsterDimensions

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
internal fun SettingsScreenContent(
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
                    .padding(KnitsterDimensions.ScreenPadding)
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