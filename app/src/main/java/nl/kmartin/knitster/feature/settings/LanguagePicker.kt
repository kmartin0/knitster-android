package nl.kmartin.knitster.feature.settings

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

@Composable
fun LanguagePicker(
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
