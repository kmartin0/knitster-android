package nl.kmartin.knitster.feature.projectlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nl.kmartin.knitster.R
import nl.kmartin.knitster.data.repository.ProjectRepository
import nl.kmartin.knitster.data.repository.ThemeRepository
import nl.kmartin.knitster.theme.ThemeColor
import nl.kmartin.knitster.theme.ThemeMode
import nl.kmartin.knitster.ui.UiText
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

private const val TAG = "ProjectListViewModel"

@HiltViewModel
class ProjectListViewModel @Inject constructor(
    private val projectRepository: ProjectRepository,
    private val themeRepository: ThemeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProjectListUiState(isLoadingProjects = true))
    val uiState: StateFlow<ProjectListUiState> = _uiState.asStateFlow()

    init {
        observeProjects()
        observeThemeMode()
        observeThemeColor()
    }

    /**
     * Creates a new, empty project and exposes its ID through [ProjectListUiState.createdProjectId].
     *
     * Creation errors are exposed through [ProjectListUiState.createProjectErrorMsg]
     * and cleared via [clearCreateProjectErrorMsg].
     */
    fun createNewProject() {
        viewModelScope.launch {
            _uiState.update { it.copy(isCreatingProject = true) }
            try {
                val id = projectRepository.insertEmptyProject()
                check(id > 0) {
                    "Insert returned invalid id: $id"
                }
                _uiState.update { it.copy(createdProjectId = id, isCreatingProject = false) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Failed to create a new project", e)
                _uiState.update {
                    it.copy(
                        isCreatingProject = false,
                        createProjectErrorMsg = UiText.Resource(R.string.error_create_project)
                    )
                }
            }
        }
    }

    /**
     * Clears the project creation error message.
     */
    fun clearCreateProjectErrorMsg() {
        _uiState.update { it.copy(createProjectErrorMsg = null) }
    }

    /**
     * Clears the created project ID.
     */
    fun clearCreatedProjectId() {
        _uiState.update { it.copy(createdProjectId = null) }
    }

    fun setThemeColor(newThemeColor: ThemeColor) {
        viewModelScope.launch {
            try {
                themeRepository.setThemeColor(newThemeColor)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Failed to save theme colour", e)

                _uiState.update {
                    it.copy(settingsErrorMsg = UiText.Resource(R.string.error_update_theme_colour))
                }
            }
        }
    }

    fun setThemeMode(newThemeMode: ThemeMode) {
        viewModelScope.launch {
            try {
                themeRepository.setThemeMode(newThemeMode)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Failed to save theme mode", e)

                _uiState.update {
                    it.copy(settingsErrorMsg = UiText.Resource(R.string.error_update_theme_mode))
                }
            }
        }
    }

    fun clearSettingsErrorMsg() {
        _uiState.update { it.copy(settingsErrorMsg = null) }
    }

    /**
     * Observes projects from the repository and updates the UI state.
     */
    private fun observeProjects() {
        viewModelScope.launch {
            projectRepository.observeProjects().collect { projects ->
                _uiState.update {
                    it.copy(
                        projects = projects,
                        showEmptyState = projects.isEmpty(),
                        isLoadingProjects = false
                    )
                }
            }
        }
    }

    /**
     * Observes theme color from the repository and updates the UI state.
     */
    private fun observeThemeColor() {
        viewModelScope.launch {
            themeRepository.observeThemeColor.collect { themeColor ->
                _uiState.update {
                    it.copy(themeColor = themeColor)
                }
            }
        }
    }

    /**
     * Observes theme mode from the repository and updates the UI state.
     */
    private fun observeThemeMode() {
        viewModelScope.launch {
            themeRepository.observeThemeMode.collect { themeMode ->
                _uiState.update {
                    it.copy(themeMode = themeMode)
                }
            }
        }
    }
}