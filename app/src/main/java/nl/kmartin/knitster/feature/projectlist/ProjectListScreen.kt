package nl.kmartin.knitster.feature.projectlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import nl.kmartin.knitster.R
import nl.kmartin.knitster.data.model.Project
import nl.kmartin.knitster.data.model.ProjectIcon
import nl.kmartin.knitster.theme.Dimensions
import nl.kmartin.knitster.theme.KnitsterBorder
import nl.kmartin.knitster.theme.knitsterTopAppBarColors
import nl.kmartin.knitster.ui.toDisplayStringOrNull
import nl.kmartin.knitster.ui.component.AppBarCircularProgressIndicator
import nl.kmartin.knitster.ui.component.ObserveSnackbarError
import nl.kmartin.knitster.ui.toDisplayString
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
    val projectListState = rememberLazyListState()
    var showSettingsBottomSheet by rememberSaveable { mutableStateOf(false) }

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

    ObserveSnackbarError(
        errorMessage = uiState.settingsErrorMsg.toDisplayStringOrNull(),
        snackbarHostState = snackbarHostState,
        onErrorShown = viewModel::clearSettingsErrorMsg
    )

    ObserveProjectListChanges(
        projects = uiState.projects,
        listState = projectListState,
    )

    if (showSettingsBottomSheet) {
        ProjectListSettingsBottomSheet(
            currentThemeColor = uiState.themeColor,
            currentThemeMode = uiState.themeMode,
            onThemeColorSelected = viewModel::setThemeColor,
            onThemeModeSelected = viewModel::setThemeMode,
            onDismiss = { showSettingsBottomSheet = false }
        )
    }

    ProjectListContent(
        uiState = uiState,
        onCreateProjectClick = viewModel::createNewProject,
        onProjectClick = onNavigateToProjectDetail,
        onSettingsClick = { showSettingsBottomSheet = true },
        snackbarHostState = snackbarHostState,
        modifier = modifier,
        projectListState = projectListState
    )
}

/**
 * Displays the project list content.
 */
@Composable
private fun ProjectListContent(
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
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = stringResource(R.string.create_project)
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
    listState: LazyListState,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        state = listState,
        contentPadding = PaddingValues(Dimensions.ScreenPadding),
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
        Text(stringResource(R.string.project_list_empty))
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
 * @param listState State of the LazyColumn to scroll.
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
