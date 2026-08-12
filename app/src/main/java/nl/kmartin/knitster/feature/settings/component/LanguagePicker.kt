package nl.kmartin.knitster.feature.settings.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.kmartin.knitster.R
import nl.kmartin.knitster.data.model.LanguageMode

/**
 * Displays the available language options and allows selecting the application language.
 *
 * @param currentLanguageMode Currently selected language mode.
 * @param onLanguageModeSelected Called with the language mode selected by the user.
 */
@Composable
internal fun LanguagePicker(
    currentLanguageMode: LanguageMode,
    onLanguageModeSelected: (LanguageMode) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(R.string.settings_language),
            style = MaterialTheme.typography.titleLarge
        )
        LanguageMode.entries.forEach { languageMode ->
            SettingsSelectableItem(
                label = stringResource(languageMode.displayNameRes),
                isSelected = languageMode == currentLanguageMode,
                onClick = { onLanguageModeSelected(languageMode) }
            )
        }
    }
}

// --- Preview ---
@Preview(showBackground = true)
@Composable
private fun LanguagePickerPreview() {
    LanguagePicker(
        currentLanguageMode = LanguageMode.DEFAULT,
        onLanguageModeSelected = {}
    )
}
