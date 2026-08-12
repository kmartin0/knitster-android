package nl.kmartin.knitster.feature.projectlist.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import nl.kmartin.knitster.R


/**
 * Displays the empty state shown when no projects exist.
 *
 * @param modifier Modifier to be applied to the root layout.
 */
@Composable
internal fun ProjectListEmptyState(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.padding(16.dp),
    ) {
        Text(stringResource(R.string.project_list_empty))
    }
}