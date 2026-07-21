package nl.kmartin.knitster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import nl.kmartin.knitster.feature.home.ProjectListScreen
import nl.kmartin.knitster.theme.KnitsterTheme

class MainActivity : ComponentActivity() {
    /**
     * Initializes the application's Compose UI.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Draw behind the system bars.
        enableEdgeToEdge()

        // Create the root Compose hierarchy, applying the app theme.
        setContent {
            KnitsterTheme {
                ProjectListScreen(
                    onProjectCreated = {},
                    onProjectClick = {}
                )
            }
        }
    }
}