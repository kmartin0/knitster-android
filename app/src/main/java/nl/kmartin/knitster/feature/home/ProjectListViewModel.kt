package nl.kmartin.knitster.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nl.kmartin.knitster.data.repository.ProjectRepository
import javax.inject.Inject

@HiltViewModel
class ProjectListViewModel @Inject constructor(
    private val projectRepository: ProjectRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProjectListUiState(isLoadingProjects = true))
    val uiState: StateFlow<ProjectListUiState> = _uiState.asStateFlow()

    init {
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

    fun createNewProject() {
        viewModelScope.launch {
            _uiState.update { it.copy(isCreatingProject = true) }
            val id = projectRepository.insertEmptyProject()
            _uiState.update { it.copy(navigateToProjectId = id, isCreatingProject = false) }
        }
    }

    fun onProjectClicked(projectId: Long) {
        _uiState.update { it.copy(navigateToProjectId = projectId) }
    }

    fun onNavigationHandled() {
        _uiState.update { it.copy(navigateToProjectId = null) }
    }
}