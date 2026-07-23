package nl.kmartin.knitster.feature.projectdetail.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import nl.kmartin.knitster.R
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
    isLoading: Boolean
) {
    TopAppBar(
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = "Back"
                )
            }
        },
        title = { },
        actions = {
            if (isLoading) AppBarCircularProgressIndicator()
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun ProjectDetailTopAppBarPreview() {
    ProjectDetailTopAppBar(
        onBackClick = {},
        isLoading = false
    )
}