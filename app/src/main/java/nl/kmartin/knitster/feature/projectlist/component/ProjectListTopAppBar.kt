package nl.kmartin.knitster.feature.projectlist.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import nl.kmartin.knitster.R
import nl.kmartin.knitster.theme.KnitsterBorder
import nl.kmartin.knitster.theme.knitsterTopAppBarColors
import nl.kmartin.knitster.ui.component.AppBarCircularProgressIndicator

/**
 * Displays the top app bar for the project list screen.
 *
 * Shows the screen title, settings and project creation actions, and a loading
 * indicator while project operations are in progress.
 *
 * @param onCreateProjectClick Called when a new project should be created.
 * @param onSettingsClick Called when the settings screen should be opened.
 * @param isLoading Whether a project operation is currently in progress.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProjectListTopAppBar(
    onCreateProjectClick: () -> Unit,
    onSettingsClick: () -> Unit,
    isLoading: Boolean
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.project_list_title)
            )
        },
        colors = knitsterTopAppBarColors(),
        actions = {
            if (isLoading) AppBarCircularProgressIndicator()
            IconButton(
                onClick = onSettingsClick
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_settings),
                    contentDescription = stringResource(R.string.settings)
                )
            }
            FilledTonalIconButton(
                onClick = onCreateProjectClick,
                enabled = !isLoading,
                shape = KnitsterBorder.Shape
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_plus),
                    contentDescription = stringResource(R.string.create_project)
                )
            }
        }
    )
}