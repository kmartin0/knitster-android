package nl.kmartin.knitster.feature.projectlist

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import nl.kmartin.knitster.data.model.Project
import nl.kmartin.knitster.ui.component.ObserveSnackbarError
import nl.kmartin.knitster.ui.toDisplayStringOrNull

/**
 * Displays the project list screen and coordinates its UI state and side effects.
 *
 * Observes the project list from [ProjectListViewModel], handles navigation after
 * project creation, displays project creation errors, keeps the list positioned
 * on the most recently saved project, and forwards user actions to the appropriate
 * handlers.
 *
 * @param onNavigateToProjectDetail Called with the project ID when a project should be opened.
 * @param onNavigateToSettings Called when the settings screen should be opened.
 * @param modifier Modifier to be applied to the screen content.
 * @param viewModel ViewModel providing the screen state and handling project actions.
 */
@Composable
fun ProjectListScreen(
    onNavigateToProjectDetail: (Long) -> Unit,
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProjectListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val projectListState = rememberLazyListState()

    ObserveCreatedProject(
        createdProjectId = uiState.createdProjectId,
        onCreatedProject = onNavigateToProjectDetail,
        onCreatedProjectHandled = viewModel::clearCreatedProjectId
    )

    ObserveSnackbarError(
        errorMessage = uiState.createProjectErrorMsg.toDisplayStringOrNull(),
        snackbarHostState = snackbarHostState,
        onErrorShown = viewModel::clearCreateProjectErrorMsg
    )

    ObserveProjectListChanges(
        projects = uiState.projects,
        listState = projectListState,
    )

    ProjectListContent(
        uiState = uiState,
        onCreateProjectClick = viewModel::createNewProject,
        onProjectClick = onNavigateToProjectDetail,
        onSettingsClick = onNavigateToSettings,
        snackbarHostState = snackbarHostState,
        modifier = modifier,
        projectListState = projectListState
    )
}

/**
 * Observes the ID of a newly created project and navigates to it once available.
 *
 * After navigation, [onCreatedProjectHandled] is invoked to clear the pending
 * project ID and prevent the navigation event from being handled again.
 *
 * @param createdProjectId ID of the newly created project, or `null` when none is pending.
 * @param onCreatedProject Called with the created project ID when navigation should occur.
 * @param onCreatedProjectHandled Called after the created project has been handled.
 */
@Composable
private fun ObserveCreatedProject(
    createdProjectId: Long?,
    onCreatedProject: (Long) -> Unit,
    onCreatedProjectHandled: () -> Unit,
) {
    LaunchedEffect(createdProjectId) {
        createdProjectId?.let { projectId ->
            onCreatedProject(projectId)
            onCreatedProjectHandled()
        }
    }
}

/**
 * Observes changes to the first project in the ordered list and scrolls the list
 * back to the top when it changes.
 *
 * Projects are ordered by [Project.lastSavedAt], so a change to the first project
 * indicates that a different project has become the most recently saved.
 *
 * @param projects Current ordered list of projects.
 * @param listState State of the project list to scroll.
 */
@Composable
private fun ObserveProjectListChanges(
    projects: List<Project>,
    listState: LazyListState,
) {
    var prevLastSavedProjectId by rememberSaveable { mutableStateOf<Long?>(null) }

    LaunchedEffect(projects) {
        if (projects.isEmpty()) {
            prevLastSavedProjectId = null
            return@LaunchedEffect
        }

        val curLastSavedProjectId = projects.first().id
        if (curLastSavedProjectId != prevLastSavedProjectId) {
            listState.animateScrollToItem(0)
        }
        prevLastSavedProjectId = curLastSavedProjectId
    }
}
