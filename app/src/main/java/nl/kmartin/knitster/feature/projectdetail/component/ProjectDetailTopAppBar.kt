package nl.kmartin.knitster.feature.projectdetail.component

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import nl.kmartin.knitster.R
import nl.kmartin.knitster.theme.knitsterTopAppBarColors
import nl.kmartin.knitster.ui.component.AppBarCircularProgressIndicator

/**
 * Displays the top app bar for the project detail screen.
 *
 * @param modifier Modifier to be applied to the top app bar.
 * @param onBackClick Called when the back button is clicked.
 * @param isLoading Whether to display a loading indicator in the app bar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProjectDetailTopAppBar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit,
    isLoading: Boolean
) {
    TopAppBar(
        modifier = modifier,
        colors = knitsterTopAppBarColors(),
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = stringResource(R.string.back),
                )
            }
        },
        title = { },
        actions = {
            if (isLoading) AppBarCircularProgressIndicator()

            ProjectDetailOverflowMenu(
                onDeleteClick = onDeleteClick
            )
        }
    )
}

@Composable
private fun ProjectDetailOverflowMenu(
    onDeleteClick: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { showMenu = true }) {
            Icon(
                painter = painterResource(R.drawable.ic_more_vert),
                contentDescription = stringResource(R.string.more_options)
            )
        }

        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false },
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.delete_project)) },
                onClick = {
                    showMenu = false
                    onDeleteClick()
                },
            )
        }
    }
}

// --- Preview ---
@Preview(showBackground = true)
@Composable
private fun ProjectDetailTopAppBarPreview() {
    ProjectDetailTopAppBar(
        onBackClick = {},
        onDeleteClick = {},
        isLoading = false
    )
}