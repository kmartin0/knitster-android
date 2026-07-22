package nl.kmartin.knitster.navigation

import android.util.Log
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
            onNavigateToProjectDetail = { projectId ->
                //TODO: Navigate the project detail
            }
        )
    }
}