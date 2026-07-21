package nl.kmartin.knitster.feature.home

import nl.kmartin.knitster.data.model.Project

data class ProjectListUiState(
    val projects: List<Project> = emptyList(),
    val showEmptyState: Boolean = false,
    val isLoading: Boolean = false
)