package nl.kmartin.knitster.feature.projectlist.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import nl.kmartin.knitster.data.model.Project
import nl.kmartin.knitster.theme.KnitsterDimensions

/**
 * Displays the projects in a vertically scrolling list.
 *
 * Each project is displayed as a clickable card using its ID as a stable list key.
 *
 * @param projects Projects to display.
 * @param onProjectClick Called with the project ID when a project is selected.
 * @param listState State controlling the project list scroll position.
 * @param modifier Modifier to be applied to the list.
 */
@Composable
internal fun ProjectList(
    projects: List<Project>,
    onProjectClick: (Long) -> Unit,
    listState: LazyListState,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        state = listState,
        contentPadding = PaddingValues(KnitsterDimensions.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = projects,
            key = { project -> project.id }
        ) { project ->
            ProjectCard(
                modifier = Modifier.animateItem(),
                project = project,
                onClick = { onProjectClick(project.id) }
            )
        }
    }
}