package nl.kmartin.knitster.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import nl.kmartin.knitster.R
import nl.kmartin.knitster.data.model.Project
import nl.kmartin.knitster.data.model.ProjectIcon
import java.time.Clock
import java.time.Instant


@Composable
fun ProjectListScreen(
    onProjectCreated: (Long) -> Unit,
    onProjectClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProjectListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ProjectListContent(
        uiState = uiState,
        onProjectClick = onProjectClick,
        onCreateProjectClick = {},
        modifier = modifier
    )
}

@Composable
private fun ProjectListContent(
    uiState: ProjectListUiState,
    onCreateProjectClick: () -> Unit,
    onProjectClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            ProjectListTopAppBar(
                onCreateProjectClick = onCreateProjectClick
            )
        }
    ) { innerPadding ->
        ProjectList(
            projects = uiState.projects,
            onProjectClick = onProjectClick,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProjectListTopAppBar(
    onCreateProjectClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "My knits"
            )
        },
        actions = {
            IconButton(
                onClick = onCreateProjectClick
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = "Create"
                )
            }
        }
    )
}

@Composable
private fun ProjectList(
    projects: List<Project>,
    onProjectClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(8.dp),
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

// --- Preview ---
@Preview(showBackground = true)
@Composable
private fun ProjectListContentPreview() {
    ProjectListContent(
        uiState = ProjectListUiState(
            projects = listOf(
                Project(id = 1, name = "Simple Socks", icon = ProjectIcon.DEFAULT, lastSavedAt = Instant.now(), createdAt = Instant.now()),
                Project(id = 2, name = "Cozy Mittens", icon = ProjectIcon.DEFAULT, lastSavedAt = Instant.now(), createdAt = Instant.now()),
                Project(id = 3, name = "Baby Blanket", icon = ProjectIcon.DEFAULT, lastSavedAt = Instant.now(), createdAt = Instant.now()),
            )
        ),
        onCreateProjectClick = {},
        onProjectClick = {}
    )
}
