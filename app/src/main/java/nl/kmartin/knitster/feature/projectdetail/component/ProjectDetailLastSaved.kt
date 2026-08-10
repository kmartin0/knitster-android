package nl.kmartin.knitster.feature.projectdetail.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import nl.kmartin.knitster.R
import nl.kmartin.knitster.theme.FontFeatures
import nl.kmartin.knitster.util.toFormattedLastSavedString
import java.time.Instant

/**
 * Displays the timestamp of when the project was last saved.
 *
 * @param modifier Modifier to be applied to the text.
 * @param lastSavedAt The instant at which the project was last saved.
 */
@Composable
internal fun ProjectDetailLastSaved(
    modifier: Modifier = Modifier,
    lastSavedAt: Instant
) {
    val locale = LocalConfiguration.current.locales[0]

    Text(
        modifier = modifier.fillMaxWidth(),
        text = stringResource(R.string.last_saved, lastSavedAt.toFormattedLastSavedString(locale)),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.labelLarge.copy(
            fontFeatureSettings = FontFeatures.TABULAR_NUMS
        ),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

// --- Preview ---
@Preview(showBackground = true)
@Composable
private fun ProjectDetailLastSavedPreview() {
    ProjectDetailLastSaved(
        lastSavedAt = Instant.parse("2026-07-23T14:30:00Z")
    )
}