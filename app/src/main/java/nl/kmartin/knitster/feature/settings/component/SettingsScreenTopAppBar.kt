package nl.kmartin.knitster.feature.settings.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import nl.kmartin.knitster.R
import nl.kmartin.knitster.theme.knitsterTopAppBarColors
import nl.kmartin.knitster.ui.component.AppBarCircularProgressIndicator

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
internal fun SettingsScreenTopAppBar(
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