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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nl.kmartin.knitster.data.model.Project
import nl.kmartin.knitster.data.repository.ProjectRepository
import nl.kmartin.knitster.navigation.ProjectDetailDestination
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration.Companion.milliseconds

private const val TAG = "ProjectDetailViewModel"

/**
 * Manages the state and user interactions for the project detail screen.
 *
 * The project is loaded using the project ID from [SavedStateHandle]. Changes to
 * the project name and notes are observed and saved after a short debounce.
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
     * Starts observing the project and user edits.
     */
    init {
        observeProject()
        observeProjectTextChanges()
    }

    /**
     * Increments the current project's row counter by one.
     */
    fun onIncrementRowCounter() {
        val currentProject = _uiState.value.project ?: return
        saveProject(
            currentProject.copy(
                rowCount = currentProject.rowCount + 1
            )
        )
    }

    /**
     * Decrements the current project's row counter by one.
     *
     * The counter is not allowed to go below zero.
     */
    fun onDecrementRowCounter() {
        val currentProject = _uiState.value.project ?: return
        if (currentProject.rowCount <= 0) return
        saveProject(
            currentProject.copy(
                rowCount = currentProject.rowCount - 1
            )
        )
    }

    /**
     * Marks the pending save error as shown.
     */
    fun onSaveProjectErrorShown() {
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
     * Observes changes to the editable project fields and automatically saves them.
     *
     * Changes are debounced to avoid saving after every keystroke.
     */
    private fun observeProjectTextChanges() {
        viewModelScope.launch {
            val titleFlow = snapshotFlow {
                projectNameState.text.toString()
            }
            val notesFlow = snapshotFlow {
                projectNotesState.text.toString()
            }

            combine(titleFlow, notesFlow) { title, notes ->
                title to notes
            }
                .debounce(500.milliseconds)
                .collect { (newTitle, newNotes) ->
                    val currentProject = _uiState.value.project ?: return@collect

                    if (
                        currentProject.name != newTitle ||
                        currentProject.notes != newNotes
                    ) {
                        saveProject(
                            currentProject.copy(
                                name = newTitle,
                                notes = newNotes
                            )
                        )
                    }
                }
        }
    }

    /**
     * Updates the project in the repository.
     *
     * The project is expected to exist, so updating zero rows is treated as an error.
     * Save errors are exposed through the UI state.
     */
    private fun saveProject(project: Project) {
        viewModelScope.launch {
            try {
                check(projectRepository.updateProject(project) > 0) {
                    "Project ${project.id} was not updated."
                }
            // Rethrow cancellation so the coroutine can be canceled normally.
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Failed to save the update of project ${project.id}", e)
                _uiState.update { it.copy(saveProjectErrorMsg = "Failed saving the update.") }
            }
        }
    }
}