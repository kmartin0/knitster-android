package nl.kmartin.knitster.feature.settings.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import nl.kmartin.knitster.R

/**
 * Displays application information at the bottom of the settings screen.
 *
 * @param versionName Current application version name to display.
 * @param modifier Modifier to be applied to the text.
 */
@Composable
internal fun SettingsAppInfo(
    versionName: String,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier.fillMaxWidth(),
        text = stringResource(R.string.settings_app_version, versionName),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center
    )
}

// --- Preview ---
@Preview(showBackground = true)
@Composable
private fun SettingsAppInfoPreview() {
    SettingsAppInfo(
        versionName = "1.1"
    )
}