package nl.kmartin.knitster.feature.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import nl.kmartin.knitster.data.model.Project
import nl.kmartin.knitster.data.model.ProjectIcon
import javax.inject.Inject

@HiltViewModel
class ProjectListViewModel @Inject constructor() : ViewModel() {
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

    private val _uiState = MutableStateFlow(
        ProjectListUiState(
            projects = sampleProjects
        )
    )

    val uiState: StateFlow<ProjectListUiState> = _uiState.asStateFlow()
}