package nl.kmartin.knitster.feature.projectdetail

import nl.kmartin.knitster.data.model.Project
import nl.kmartin.knitster.ui.UiText

/**
 * Represents the UI state for the project detail screen.
 */
data class ProjectDetailUiState(
    val project: Project? = null,
    val isLoadingProject: Boolean = true,
    val saveProjectErrorMsg: UiText? = null,
    val deleteProjectErrorMsg: UiText? = null,
    val undoResetRowCounter: UndoResetRowCounter? = null,
    val projectDeleted: Boolean = false
)

data class UndoResetRowCounter(
    val rowCounterId: Long,
    val previousCount: Int
)