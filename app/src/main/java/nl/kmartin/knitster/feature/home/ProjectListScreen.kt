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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.kmartin.knitster.R
import nl.kmartin.knitster.data.model.Project
import nl.kmartin.knitster.data.model.ProjectIcon

@Composable
fun ProjectListScreen(
    onProjectCreated: (Long) -> Unit,
    onProjectClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    // Temporary hardcoded projects
    val projects = sampleProjects

    ProjectListContent(
        projects = projects,
        onProjectClick = onProjectClick,
        onCreateProjectClick = {},
        modifier = modifier
    )
}

@Composable
fun ProjectListContent(
    projects: List<Project>,
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
            projects = projects,
            onProjectClick = onProjectClick,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectListTopAppBar(
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
fun ProjectList(
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
fun ProjectListContentPreview() {
    ProjectListContent(
        projects = sampleProjects,
        onCreateProjectClick = {},
        onProjectClick = {}
    )
}

private val sampleProjects = listOf(
    Project(
        id = 1,
        name = "Cozy Sweater",
        icon = ProjectIcon.SWEATER,
        notes = "A warm and cozy sweater knit with alpaca wool.",
        rowCount = 128
    ),
    Project(id = 2, name = "Winter Hat", icon = ProjectIcon.SWEATER),
    Project(id = 3, name = "Chunky Scarf", icon = ProjectIcon.SWEATER),
    Project(id = 4, name = "Simple Socks", icon = ProjectIcon.SWEATER),
    Project(id = 5, name = "Cozy Mittens", icon = ProjectIcon.SWEATER),
    Project(id = 6, name = "Baby Blanket", icon = ProjectIcon.SWEATER),
)
