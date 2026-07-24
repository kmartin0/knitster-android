package nl.kmartin.knitster.feature.projectlist

import nl.kmartin.knitster.data.model.Project

data class ProjectListUiState(
    val projects: List<Project> = emptyList(),
    val showEmptyState: Boolean = false,
    val isLoadingProjects: Boolean = false,
    val isCreatingProject: Boolean = false,
    val createdProjectId: Long? = null,
    val createProjectErrorMsg: String? = null
)