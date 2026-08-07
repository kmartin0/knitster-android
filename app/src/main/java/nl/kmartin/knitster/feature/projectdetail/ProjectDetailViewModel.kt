package nl.kmartin.knitster.feature.projectdetail

import android.util.Log
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nl.kmartin.knitster.R
import nl.kmartin.knitster.data.model.Project
import nl.kmartin.knitster.data.model.ProjectIcon
import nl.kmartin.knitster.data.model.RowCounter
import nl.kmartin.knitster.data.repository.ProjectRepository
import nl.kmartin.knitster.navigation.ProjectDetailDestination
import nl.kmartin.knitster.ui.UiText
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration.Companion.milliseconds

private const val TAG = "ProjectDetailViewModel"

/**
 * Manages the state and user interactions for the project detail screen.
 *
 * The project is loaded using the project ID from [SavedStateHandle]. Changes to
 * the project name and notes are observed and saved after a short debounce.
 * Row counters (add, update, delete, increment, decrement, reset with undo) are
 * persisted immediately via direct repository calls.
 *
 * @param savedStateHandle Provides the navigation arguments for this destination.
 * @param projectRepository Repository used to observe and update projects.
 */
@OptIn(FlowPreview::class)
@HiltViewModel
class ProjectDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val projectRepository: ProjectRepository
) : ViewModel() {
    private val projectId: Long = savedStateHandle.toRoute<ProjectDetailDestination>().projectId
    private val _uiState = MutableStateFlow(ProjectDetailUiState(isLoadingProject = true))

    /** Current state of the project detail screen. */
    val uiState: StateFlow<ProjectDetailUiState> = _uiState.asStateFlow()

    /** State backing the editable project name field. */
    val projectNameState = TextFieldState()

    /** State backing the editable project notes field. */
    val projectNotesState = TextFieldState()

    /**
     * Initializes observation of the current project and text-field changes.
     */
    init {
        observeProject()

        observeTextChanges(
            textFieldState = projectNameState,
            currentValue = Project::name,
            onTextChanged = projectRepository::updateProjectName
        )

        observeTextChanges(
            textFieldState = projectNotesState,
            currentValue = Project::notes,
            onTextChanged = projectRepository::updateProjectNotes
        )
    }

    /**
     * Adds a new row counter to the current project.
     *
     * @param rowCounter Row counter to add.
     */
    fun addRowCounter(rowCounter: RowCounter) {
        val currentProject = _uiState.value.project ?: return

        executeRepositoryOperation(
            projectId = currentProject.id,
            operation = {
                projectRepository.addRowCounter(
                    projectId = currentProject.id,
                    rowCounter = rowCounter
                )
            },
            isSuccess = { it > 0L }
        )
    }

    /**
     * Increments the specified row counter by one.
     *
     * @param rowCounterId ID of the row counter to increment.
     */
    fun incrementRowCounter(rowCounterId: Long) {
        val (currentProject, rowCounter) = findRowCounter(rowCounterId) ?: return

        executeRepositoryOperation(
            projectId = currentProject.id,
            operation = {
                projectRepository.updateRowCounterCount(
                    id = rowCounterId,
                    count = rowCounter.count + 1,
                    projectId = currentProject.id
                )
            },
            isSuccess = { it > 0 }
        )
    }

    /**
     * Decrements the specified row counter by one.
     * The operation is ignored when the counter is already zero.
     *
     * @param rowCounterId ID of the row counter to decrement.
     */
    fun decrementRowCounter(rowCounterId: Long) {
        val (currentProject, rowCounter) = findRowCounter(rowCounterId) ?: return
        if (rowCounter.count <= 0) return

        executeRepositoryOperation(
            projectId = currentProject.id,
            operation = {
                projectRepository.updateRowCounterCount(
                    id = rowCounterId,
                    count = rowCounter.count - 1,
                    projectId = currentProject.id
                )
            },
            isSuccess = { it > 0 }
        )
    }

    /**
     * Resets the specified row counter to zero.
     *
     * The previous count is retained so the reset can be undone.
     * The operation is ignored when the counter is already zero.
     *
     * @param rowCounterId ID of the row counter to reset.
     */
    fun resetRowCounter(rowCounterId: Long) {
        val (currentProject, rowCounter) = findRowCounter(rowCounterId) ?: return
        val previousCount = rowCounter.count
        if (previousCount == 0) return

        executeRepositoryOperation(
            projectId = currentProject.id,
            operation = {
                projectRepository.updateRowCounterCount(
                    id = rowCounterId,
                    projectId = currentProject.id,
                    count = 0
                )
            },
            isSuccess = { it > 0 },
            onSuccess = {
                _uiState.update {
                    it.copy(
                        undoResetRowCounter = UndoResetRowCounter(
                            rowCounterId = rowCounterId,
                            previousCount = previousCount
                        )
                    )
                }
            }
        )
    }

    /**
     * Restores the row counter value saved by the most recent reset operation.
     * The operation is ignored when no reset is available to undo.
     */
    fun undoResetRowCounter() {
        val undoResetRowCounter = _uiState.value.undoResetRowCounter ?: return
        val currentProject = _uiState.value.project ?: return

        executeRepositoryOperation(
            projectId = currentProject.id,
            operation = {
                projectRepository.updateRowCounterCount(
                    id = undoResetRowCounter.rowCounterId,
                    projectId = currentProject.id,
                    count = undoResetRowCounter.previousCount
                )
            },
            isSuccess = { it > 0 },
            onSuccess = {
                _uiState.update { it.copy(undoResetRowCounter = null) }
            }
        )
    }

    /**
     * Clears the stored row count used for undoing a reset.
     */
    fun clearUndoResetRowCounter() {
        _uiState.update { it.copy(undoResetRowCounter = null) }
    }

    /**
     * Deletes the specified row counter from the current project.
     *
     * @param rowCounterId ID of the row counter to delete.
     */
    fun deleteRowCounter(rowCounterId: Long) {
        val (currentProject, rowCounter) = findRowCounter(rowCounterId) ?: return

        executeRepositoryOperation(
            projectId = currentProject.id,
            operation = {
                projectRepository.deleteRowCounter(
                    rowCounter = rowCounter,
                    projectId = currentProject.id
                )
            },
            isSuccess = { it > 0 }
        )
    }

    /**
     * Updates an existing row counter in the current project.
     *
     * @param rowCounter Updated row-counter data.
     */
    fun updateRowCounter(rowCounter: RowCounter) {
        val currentProject = _uiState.value.project ?: return

        executeRepositoryOperation(
            projectId = currentProject.id,
            operation = {
                projectRepository.updateRowCounter(
                    rowCounter = rowCounter,
                    projectId = currentProject.id
                )
            },
            isSuccess = { it > 0 }
        )
    }

    /**
     * Deletes the current project.
     *
     * The project is expected to exist, so deleting zero rows is treated as an error.
     * Delete errors are exposed through the UI state.
     */
    fun deleteProject() {
        val currentProject = _uiState.value.project ?: return
        viewModelScope.launch {
            try {
                check(projectRepository.deleteProject(currentProject) > 0) {
                    "Project ${currentProject.id} was not deleted."
                }

                _uiState.update { it.copy(projectDeleted = true) }
            } catch (e: CancellationException) {
                // Rethrow cancellation so the coroutine can be canceled normally.
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Failed to delete project ${currentProject.id}", e)

                _uiState.update {
                    it.copy(
                        deleteProjectErrorMsg = UiText.Resource(
                            R.string.error_delete_project
                        )
                    )
                }
            }
        }
    }

    /**
     * Updates the icon of the current project.
     *
     * @param newIcon New icon to assign to the project.
     */
    fun updateProjectIcon(newIcon: ProjectIcon) {
        val currentProject = _uiState.value.project ?: return

        executeRepositoryOperation(
            projectId = currentProject.id,
            operation = {
                projectRepository.updateProjectIcon(
                    projectId = currentProject.id,
                    icon = newIcon
                )
            },
            isSuccess = { it > 0 }
        )
    }

    /**
     * Clears the pending delete project error message.
     */
    fun clearDeleteProjectErrorMsg() {
        _uiState.update { it.copy(deleteProjectErrorMsg = null) }
    }

    /**
     * Clears the pending save project error message.
     */
    fun clearSaveProjectErrorShown() {
        _uiState.update { it.copy(saveProjectErrorMsg = null) }
    }

    /**
     * Observes the current project and updates the screen state.
     *
     * The editable text fields are initialized only when the project is first
     * loaded, preventing later repository emissions from overwriting user input.
     */
    private fun observeProject() {
        viewModelScope.launch {
            projectRepository.observeProject(projectId).collect { project ->
                // Populate the text fields only for the initial project emission.
                if (_uiState.value.project == null && project != null) {
                    projectNameState.setTextAndPlaceCursorAtEnd(project.name)
                    projectNotesState.setTextAndPlaceCursorAtEnd(project.notes)
                }

                _uiState.update {
                    it.copy(
                        project = project,
                        isLoadingProject = false
                    )
                }
            }
        }
    }

    /**
     * Observes changes to a text field and persists changed values after being debounced.
     *
     * Values equal to the latest project value are ignored.
     *
     * @param textFieldState Text-field state to observe.
     * @param currentValue Returns the currently persisted value from the project.
     * @param onTextChanged Persists a changed text value and returns the number of affected rows.
     */
    private fun observeTextChanges(
        textFieldState: TextFieldState,
        currentValue: (Project) -> String,
        onTextChanged: suspend (projectId: Long, text: String) -> Int,
    ) {
        viewModelScope.launch {
            snapshotFlow {
                textFieldState.text.toString()
            }
                .debounce(500.milliseconds)
                .collect { newText ->
                    val project = _uiState.value.project ?: return@collect

                    if (currentValue(project) != newText) {
                        executeRepositoryOperation(
                            projectId = project.id,
                            operation = {
                                onTextChanged(project.id, newText)
                            },
                            isSuccess = { it > 0 }
                        )
                    }
                }
        }
    }

    /**
     * Executes a repository operation and reports unsuccessful updates through UI state.
     *
     * Coroutine cancellation is rethrown so structured cancellation behaves normally.
     *
     * @param projectId ID of the project associated with the operation.
     * @param operation Repository operation to execute.
     * @param isSuccess Determines whether the returned result represents success.
     * @param onSuccess Called with the successful operation result after [isSuccess] returns `true`.
     */
    private fun <T> executeRepositoryOperation(
        projectId: Long,
        operation: suspend () -> T,
        isSuccess: (T) -> Boolean,
        onSuccess: (T) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                val result = operation()

                check(isSuccess(result)) {
                    "Operation for project $projectId did not succeed."
                }
                onSuccess(result)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Failed operation for project $projectId", e)

                _uiState.update {
                    it.copy(
                        saveProjectErrorMsg = UiText.Resource(
                            R.string.error_save_project
                        )
                    )
                }
            }
        }
    }

    /**
     * Finds a row counter in the currently loaded project.
     *
     * @param rowCounterId ID of the row counter to find.
     * @return The current project and matching row counter, or `null` when either
     * the project or counter cannot be found.
     */
    private fun findRowCounter(
        rowCounterId: Long
    ): Pair<Project, RowCounter>? {
        val currentProject = _uiState.value.project ?: return null
        val rowCounter = currentProject.rowCounters.firstOrNull {
            it.id == rowCounterId
        } ?: return null

        return currentProject to rowCounter
    }
}
