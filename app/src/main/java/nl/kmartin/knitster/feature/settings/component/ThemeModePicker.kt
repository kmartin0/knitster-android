package nl.kmartin.knitster.feature.settings.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.kmartin.knitster.R
import nl.kmartin.knitster.data.model.ThemeMode


/**
 * Displays the available brightness modes.
 *
 * The currently selected mode is highlighted and selecting a mode invokes
 * [onThemeModeSelected].
 *
 * @param currentThemeMode Currently selected brightness mode.
 * @param onThemeModeSelected Called when a brightness mode is selected.
 */
@Composable
internal fun ThemeModePicker(
    currentThemeMode: ThemeMode,
    onThemeModeSelected: (ThemeMode) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(R.string.settings_brightness),
            style = MaterialTheme.typography.titleLarge
        )
        ThemeMode.entries.forEach { themeMode ->
            SettingsSelectableItem(
                label = stringResource(themeMode.displayNameRes),
                isSelected = themeMode == currentThemeMode,
                onClick = { onThemeModeSelected(themeMode) },
                startIcon = painterResource(themeMode.drawableRes)
            )
        }
    }
}

// --- Preview ---
@Preview(showBackground = true)
@Composable
private fun ThemeModePickerPreview() {
    ThemeModePicker(
        currentThemeMode = ThemeMode.DEFAULT,
        onThemeModeSelected = {}
    )
}
