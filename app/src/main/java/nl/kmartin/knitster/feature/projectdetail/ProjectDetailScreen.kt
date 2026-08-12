package nl.kmartin.knitster.feature.projectdetail

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import nl.kmartin.knitster.R
import nl.kmartin.knitster.data.model.ProjectIcon
import nl.kmartin.knitster.data.model.RowCounter
import nl.kmartin.knitster.feature.projectdetail.component.ProjectDetailDeleteProjectDialog
import nl.kmartin.knitster.feature.projectdetail.component.ProjectDetailDeleteRowCounterDialog
import nl.kmartin.knitster.feature.projectdetail.component.ProjectDetailIconPickerBottomSheet
import nl.kmartin.knitster.feature.projectdetail.component.ProjectDetailRowCounterFormBottomSheet
import nl.kmartin.knitster.ui.component.ObserveSnackbarError
import nl.kmartin.knitster.ui.toDisplayStringOrNull

/**
 * Displays the project detail screen and connects it to the [ProjectDetailViewModel].
 *
 * @param modifier Modifier to be applied to the root layout.
 * @param viewModel ViewModel providing the screen state and handling user interactions.
 * @param onNavigateBack Called when the screen should navigate back.
 */
@Composable
fun ProjectDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: ProjectDetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var showDeleteProjectDialog by rememberSaveable { mutableStateOf(false) }
    var pendingDeleteRowCounterId by rememberSaveable { mutableStateOf<Long?>(null) }
    var pendingEditRowCounterId by rememberSaveable { mutableStateOf<Long?>(null) }
    var showIconPickerBottomSheet by rememberSaveable { mutableStateOf(false) }
    var showAddRowCounterBottomSheet by rememberSaveable { mutableStateOf(false) }

    val pendingEditRowCounter = uiState.findRowCounter(pendingEditRowCounterId)
    val pendingDeleteRowCounter = uiState.findRowCounter(pendingDeleteRowCounterId)

    ObserveSnackbarError(
        errorMessage = uiState.saveProjectErrorMsg.toDisplayStringOrNull(),
        snackbarHostState = snackbarHostState,
        onErrorShown = viewModel::clearSaveProjectErrorShown
    )

    ObserveSnackbarError(
        errorMessage = uiState.deleteProjectErrorMsg.toDisplayStringOrNull(),
        snackbarHostState = snackbarHostState,
        onErrorShown = viewModel::clearDeleteProjectErrorMsg
    )

    ObserveResetRowCounterUndo(
        uiState.undoResetRowCounter,
        snackbarHostState = snackbarHostState,
        onUndo = viewModel::undoResetRowCounter,
        onDismiss = viewModel::clearUndoResetRowCounter
    )

    ObserveProjectDeleted(
        projectDeleted = uiState.projectDeleted,
        onProjectDeleted = onNavigateBack
    )

    if (showDeleteProjectDialog) {
        ProjectDetailDeleteProjectDialog(
            onConfirm = {
                showDeleteProjectDialog = false
                viewModel.deleteProject()
            },
            onDismiss = { showDeleteProjectDialog = false },
            projectName = uiState.project?.name.orEmpty()
        )
    }

    pendingDeleteRowCounter?.let { rowCounter ->
        ProjectDetailDeleteRowCounterDialog(
            rowCounter = rowCounter,
            onConfirm = {
                pendingDeleteRowCounterId = null
                viewModel.deleteRowCounter(rowCounter.id)
            },
            onDismiss = {
                pendingDeleteRowCounterId = null
            }
        )
    }

    pendingEditRowCounter?.let { rowCounter ->
        ProjectDetailRowCounterFormBottomSheet(
            title = stringResource(R.string.edit_counter),
            rowCounter = rowCounter,
            onDismiss = {
                pendingEditRowCounterId = null
            },
            onSave = { updatedRowCounter ->
                pendingEditRowCounterId = null
                viewModel.updateRowCounter(updatedRowCounter)
            }
        )
    }

    if (showAddRowCounterBottomSheet) {
        ProjectDetailRowCounterFormBottomSheet(
            title = stringResource(R.string.add_counter),
            rowCounter = RowCounter(),
            onDismiss = {
                showAddRowCounterBottomSheet = false
            },
            onSave = { newRowCounter ->
                showAddRowCounterBottomSheet = false
                viewModel.addRowCounter(newRowCounter)
            }
        )
    }

    if (showIconPickerBottomSheet) {
        ProjectDetailIconPickerBottomSheet(
            onDismiss = { showIconPickerBottomSheet = false },
            onIconSelected = { selectedIcon ->
                viewModel.updateProjectIcon(selectedIcon)
            },
            currentIcon = uiState.project?.icon ?: ProjectIcon.DEFAULT
        )
    }

    ProjectDetailScreenContent(
        modifier = modifier,
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onBackClick = onNavigateBack,
        onDeleteClick = { showDeleteProjectDialog = true },
        onMoveCounterUpClick = viewModel::moveRowCounterUp,
        onMoveCounterDownClick = viewModel::moveRowCounterDown,
        onResetCounterClick = viewModel::resetRowCounter,
        onEditCounterClick = { pendingEditRowCounterId = it },
        onDeleteCounterClick = { pendingDeleteRowCounterId = it },
        onIncrementClick = viewModel::incrementRowCounter,
        onDecrementClick = viewModel::decrementRowCounter,
        onAddRowCounterClick = { showAddRowCounterBottomSheet = true },
        onProjectIconClick = { showIconPickerBottomSheet = true },
        projectNameState = viewModel.projectNameState,
        projectNotesState = viewModel.projectNotesState,
    )
}

/**
 * Observes row counter resets and displays an undo snackbar.
 *
 * @param undoResetRowCounter The data class containing the undo data, or `null` if no undo is pending.
 * @param snackbarHostState State used to display the snackbar.
 * @param onUndo Called when the user chooses to undo the reset.
 * @param onDismiss Called when the undo opportunity expires.
 */
@Composable
private fun ObserveResetRowCounterUndo(
    undoResetRowCounter: UndoResetRowCounter?,
    snackbarHostState: SnackbarHostState,
    onUndo: () -> Unit,
    onDismiss: () -> Unit,
) {
    val message = undoResetRowCounter?.let {
        stringResource(
            R.string.row_counter_reset_from,
            it.previousCount
        )
    }

    val undoLabel = stringResource(R.string.undo)

    LaunchedEffect(undoResetRowCounter) {
        if (message != null) {
            when (
                snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = undoLabel,
                    duration = SnackbarDuration.Short,
                )
            ) {
                SnackbarResult.ActionPerformed -> onUndo()
                SnackbarResult.Dismissed -> onDismiss()
            }
        }
    }
}

/**
 * Observes whether the current project has been deleted and invokes
 * [onProjectDeleted] once the deletion is confirmed.
 *
 * @param projectDeleted Whether the project has been deleted.
 * @param onProjectDeleted Called when the project has been deleted.
 */
@Composable
private fun ObserveProjectDeleted(
    projectDeleted: Boolean,
    onProjectDeleted: () -> Unit
) {
    LaunchedEffect(projectDeleted) {
        if (projectDeleted) onProjectDeleted()
    }
}

/**
 * Finds the row counter with the specified [id] in the current project.
 *
 * @param id ID of the row counter to find, or `null`.
 * @return The matching [RowCounter], or `null` if no matching row counter
 * exists or if [id] is `null`.
 */
private fun ProjectDetailUiState.findRowCounter(
    id: Long?
): RowCounter? {
    if (id == null) return null

    return project
        ?.rowCounters
        ?.firstOrNull { it.id == id }
}
