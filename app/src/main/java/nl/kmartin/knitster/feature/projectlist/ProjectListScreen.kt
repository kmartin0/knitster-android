package nl.kmartin.knitster.feature.projectlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import nl.kmartin.knitster.R
import nl.kmartin.knitster.data.model.Project
import nl.kmartin.knitster.data.model.ProjectIcon
import nl.kmartin.knitster.theme.KnitsterDimensions
import nl.kmartin.knitster.ui.component.AppBarCircularProgressIndicator
import nl.kmartin.knitster.ui.component.ObserveSnackbarError
import java.time.Instant

/**
 * Displays the project list screen and coordinates UI state, side effects, and user actions.
 */
@Composable
fun ProjectListScreen(
    onNavigateToProjectDetail: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProjectListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveCreatedProject(
        createdProjectId = uiState.createdProjectId,
        onCreatedProject = onNavigateToProjectDetail,
        onCreatedProjectHandled = viewModel::clearCreatedProjectId
    )

    ObserveSnackbarError(
        errorMessage = uiState.createProjectErrorMsg,
        snackbarHostState = snackbarHostState,
        onErrorShown = viewModel::clearCreateProjectErrorMsg
    )

    ProjectListContent(
        uiState = uiState,
        onCreateProjectClick = viewModel::createNewProject,
        onProjectClick = onNavigateToProjectDetail,
        snackbarHostState = snackbarHostState,
        modifier = modifier
    )
}

/**
 * Displays the project list content.
 */
@Composable
private fun ProjectListContent(
    uiState: ProjectListUiState,
    onCreateProjectClick: () -> Unit,
    onProjectClick: (Long) -> Unit,
    snackbarHostState: SnackbarHostState,
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
            )
        }
    }
}

/**
 * Navigates to the newly created project once its ID becomes available.
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
 * Displays the project list top app bar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProjectListTopAppBar(
    onCreateProjectClick: () -> Unit,
    isLoading: Boolean
) {
    TopAppBar(
        title = {
            Text(
                text = "My knits"
            )
        },
        actions = {
            if (isLoading) AppBarCircularProgressIndicator()
            IconButton(
                onClick = onCreateProjectClick,
                enabled = !isLoading
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = "Create"
                )
            }
        }
    )
}

/**
 * Displays the list of projects.
 */
@Composable
private fun ProjectList(
    projects: List<Project>,
    onProjectClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(KnitsterDimensions.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = projects,
            key = { project -> project.id }
        ) { project ->
            ProjectCard(
                project = project,
                onClick = { onProjectClick(project.id) }
            )
        }
    }
}

/**
 * Displays the empty state when no projects exist.
 */
@Composable
private fun ProjectListEmptyState(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.padding(16.dp),
    ) {
        Text("No knitting project created")
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
        snackbarHostState = remember { SnackbarHostState() }
    )
}
