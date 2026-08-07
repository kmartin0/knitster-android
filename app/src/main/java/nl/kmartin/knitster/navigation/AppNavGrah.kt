package nl.kmartin.knitster.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import nl.kmartin.knitster.feature.projectdetail.ProjectDetailScreen
import nl.kmartin.knitster.feature.projectlist.ProjectListScreen
import nl.kmartin.knitster.feature.settings.SettingsScreen

@Serializable
data object ProjectListDestination

@Serializable
data class ProjectDetailDestination(
    val projectId: Long
)

@Serializable
data object SettingsDestination

fun NavController.navigateToProjectDetailDestination(projectId: Long) {
    navigate(ProjectDetailDestination(projectId))
}

fun NavController.navigateToSettingsDestination() {
    navigate(SettingsDestination)
}

fun NavGraphBuilder.appGraph(navController: NavHostController) {
    composable<ProjectListDestination> {
        ProjectListScreen(
            onNavigateToProjectDetail = { projectId ->
                navController.navigateToProjectDetailDestination(projectId)
            },
            onNavigateToSettings = {
                navController.navigateToSettingsDestination()
            }
        )
    }

    composable<ProjectDetailDestination> {
        ProjectDetailScreen(
            onNavigateBack = {
                navController.popBackStack<ProjectListDestination>(
                    inclusive = false
                )
            }
        )
    }

    composable<SettingsDestination> {
        SettingsScreen(
            onNavigateBack = {
                navController.popBackStack<ProjectListDestination>(
                    inclusive = false
                )
            }
        )
    }
}