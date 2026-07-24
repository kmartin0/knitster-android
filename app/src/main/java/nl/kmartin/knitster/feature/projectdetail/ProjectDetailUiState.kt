package nl.kmartin.knitster.feature.projectdetail

import nl.kmartin.knitster.data.model.Project

/**
 * Represents the UI state for the project detail screen.
 */
data class ProjectDetailUiState(
    val project: Project? = null,
    val isLoadingProject: Boolean = true,
    val saveProjectErrorMsg: String? = null,
    val deleteProjectErrorMsg: String? = null,
    val rowCountBeforeReset: Int? = null,
    val projectDeleted: Boolean = false
)