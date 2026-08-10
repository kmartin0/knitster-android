package nl.kmartin.knitster.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.kmartin.knitster.R

@Composable
fun GeneralSettings(
    modifier: Modifier = Modifier,
    keepScreenAwake: Boolean,
    onKeepScreenAwakeChanged: (Boolean) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(R.string.settings_general),
            style = MaterialTheme.typography.titleLarge
        )

        KeepScreenAwakeSwitch(
            keepScreenAwake = keepScreenAwake,
            onKeepScreenAwakeChanged = onKeepScreenAwakeChanged
        )
    }
}

@Composable
private fun KeepScreenAwakeSwitch(
    modifier: Modifier = Modifier,
    keepScreenAwake: Boolean,
    onKeepScreenAwakeChanged: (Boolean) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.settings_keep_screen_awake)
        )
        Switch(
            checked = keepScreenAwake,
            onCheckedChange = onKeepScreenAwakeChanged
        )
    }
}

// --- Preview ---
@Preview(showBackground = true)
@Composable
private fun GeneralSettingsPreview() {
    GeneralSettings(
        keepScreenAwake = true,
        onKeepScreenAwakeChanged = {}
    )
}