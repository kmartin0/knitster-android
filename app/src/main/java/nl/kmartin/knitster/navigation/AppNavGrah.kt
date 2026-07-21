package nl.kmartin.knitster.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import nl.kmartin.knitster.feature.home.ProjectListScreen

@Serializable
data object ProjectListDestination

fun NavGraphBuilder.appGraph(navController: NavHostController) {
    composable<ProjectListDestination> {
        ProjectListScreen(
            onProjectCreated = { /* TODO: Navigate to the created project */ },
            onProjectClick = { /* TODO: Navigate to the clicked project */ }
        )
    }
}