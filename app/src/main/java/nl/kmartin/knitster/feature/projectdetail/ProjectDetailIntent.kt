package nl.kmartin.knitster.feature.projectdetail

import nl.kmartin.knitster.data.model.ProjectIcon
import nl.kmartin.knitster.data.model.RowCounter

sealed interface ProjectDetailIntent {
    data object SaveProjectErrorDismissed : ProjectDetailIntent
    data object DeleteProjectErrorDismissed : ProjectDetailIntent
    data object DeleteProject : ProjectDetailIntent
    data object UndoResetRowCounter : ProjectDetailIntent
    data object ResetRowCounterUndoDismissed : ProjectDetailIntent
    data object ProjectDeletedHandled : ProjectDetailIntent
    data class DeleteRowCounter(val rowCounterId: Long) : ProjectDetailIntent
    data class UpdateRowCounter(val rowCounter: RowCounter) : ProjectDetailIntent
    data class AddRowCounter(val rowCounter: RowCounter) : ProjectDetailIntent
    data class UpdateProjectIcon(val projectIcon: ProjectIcon) : ProjectDetailIntent
    data class MoveRowCounterUp(val rowCounterId: Long) : ProjectDetailIntent
    data class MoveRowCounterDown(val rowCounterId: Long) : ProjectDetailIntent
    data class ResetRowCounter(val rowCounterId: Long) : ProjectDetailIntent
    data class IncrementRowCounter(val rowCounterId: Long) : ProjectDetailIntent
    data class DecrementRowCounter(val rowCounterId: Long) : ProjectDetailIntent
}