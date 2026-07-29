package nl.kmartin.knitster.feature.projectlist

import nl.kmartin.knitster.data.model.Project
import nl.kmartin.knitster.theme.ThemeColor
import nl.kmartin.knitster.theme.ThemeMode

data class ProjectListUiState(
    val projects: List<Project> = emptyList(),
    val showEmptyState: Boolean = false,
    val isLoadingProjects: Boolean = false,
    val isCreatingProject: Boolean = false,
    val createdProjectId: Long? = null,
    val createProjectErrorMsg: String? = null,
    val settingsErrorMsg: String? = null,
    val themeColor: ThemeColor = ThemeColor.DEFAULT,
    val themeMode: ThemeMode = ThemeMode.DEFAULT,
)