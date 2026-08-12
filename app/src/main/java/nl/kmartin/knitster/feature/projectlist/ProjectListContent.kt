package nl.kmartin.knitster.feature.projectlist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import nl.kmartin.knitster.data.model.Project
import nl.kmartin.knitster.data.model.ProjectIcon
import nl.kmartin.knitster.feature.projectlist.component.ProjectList
import nl.kmartin.knitster.feature.projectlist.component.ProjectListEmptyState
import nl.kmartin.knitster.feature.projectlist.component.ProjectListTopAppBar
import java.time.Instant

/**
 * Displays the content of the project list screen.
 *
 * Shows the project list when projects are available, an empty state when no
 * projects exist, and no content while projects are loading. User actions and
 * snackbar state are forwarded to the appropriate child composables.
 *
 * @param uiState Current UI state of the project list screen.
 * @param onCreateProjectClick Called when a new project should be created.
 * @param onSettingsClick Called when the settings screen should be opened.
 * @param onProjectClick Called with the project ID when a project is selected.
 * @param snackbarHostState State used to display snackbar messages.
 * @param projectListState State controlling the project list scroll position.
 * @param modifier Modifier to be applied to the root scaffold.
 */
@Composable
internal fun ProjectListContent(
    uiState: ProjectListUiState,
    onCreateProjectClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onProjectClick: (Long) -> Unit,
    snackbarHostState: SnackbarHostState,
    projectListState: LazyListState,
    modifier: Modifier = Modifier
) {
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        modifier = modifier,
        topBar = {
            ProjectListTopAppBar(
                onCreateProjectClick = onCreateProjectClick,
                onSettingsClick = onSettingsClick,
                isLoading = uiState.isLoadingProjects || uiState.isCreatingProject
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            if (uiState.isLoadingProjects) {
                return@Box
            }

            if (uiState.showEmptyState) {
                ProjectListEmptyState()
                return@Box
            }

            ProjectList(
                projects = uiState.projects,
                onProjectClick = onProjectClick,
                listState = projectListState
            )
        }
    }
}

// --- Preview ---
@Preview(showBackground = true)
@Composable
private fun ProjectListContentPreview() {
    ProjectListContent(
        uiState = ProjectListUiState(
            projects = listOf(
                Project(
                    id = 1,
                    name = "Simple Socks",
                    icon = ProjectIcon.DEFAULT,
                    lastSavedAt = Instant.now(),
                    createdAt = Instant.now()
                ),
                Project(
                    id = 2,
                    name = "Cozy Mittens",
                    icon = ProjectIcon.DEFAULT,
                    lastSavedAt = Instant.now(),
                    createdAt = Instant.now()
                ),
                Project(
                    id = 3,
                    name = "Baby Blanket",
                    icon = ProjectIcon.DEFAULT,
                    lastSavedAt = Instant.now(),
                    createdAt = Instant.now()
                ),
            )
        ),
        onCreateProjectClick = {},
        onProjectClick = {},
        onSettingsClick = {},
        snackbarHostState = remember { SnackbarHostState() },
        projectListState = rememberLazyListState()
    )
}