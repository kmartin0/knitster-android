package nl.kmartin.knitster.main

import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import nl.kmartin.knitster.data.model.resolveDarkTheme
import nl.kmartin.knitster.navigation.AppNavigation
import nl.kmartin.knitster.theme.KnitsterTheme
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainActivityViewModel by viewModels()

    /**
     * Initializes the activity and its Compose UI.
     *
     * Installs the splash screen, keeps it visible until the theme is loaded and
     * the minimum display duration has elapsed, applies edge-to-edge system bar
     * styling, and creates the root Compose hierarchy using the persisted app theme.
     *
     * @param savedInstanceState Previously saved activity state, or `null` when the
     * activity is being created for the first time.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var isSplashMinDurationElapsed = false
        var isThemeLoaded = false

        splashScreen.setKeepOnScreenCondition {
            !isSplashMinDurationElapsed || !isThemeLoaded
        }

        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val darkTheme = uiState.themeMode.resolveDarkTheme(isSystemInDarkTheme())

            ObserveSplashScreenConditions(
                themeLoaded = uiState.themeLoaded,
                onThemeLoaded = { isThemeLoaded = it },
                onMinDurationElapsed = { isSplashMinDurationElapsed = true },
            )

            ApplyEdgeToEdgeStyle(darkTheme)

            KnitsterTheme(
                themeColor = uiState.themeColor,
                themeMode = uiState.themeMode
            ) {
                AppNavigation()
            }
        }
    }

    /**
     * Observes the conditions that determine when the splash screen can be dismissed.
     *
     * The splash screen remains visible until both the minimum display duration has
     * elapsed and the application's theme has finished loading.
     *
     * @param themeLoaded Whether the application theme has been loaded.
     * @param onThemeLoaded Called whenever the theme loaded state changes.
     * @param onMinDurationElapsed Called once the minimum splash screen duration has elapsed.
     */
    @Composable
    private fun ObserveSplashScreenConditions(
        themeLoaded: Boolean,
        onThemeLoaded: (Boolean) -> Unit,
        onMinDurationElapsed: () -> Unit,
    ) {
        LaunchedEffect(Unit) {
            delay(2.seconds)
            onMinDurationElapsed()
        }

        SideEffect {
            onThemeLoaded(themeLoaded)
        }
    }

    /**
     * Applies the appropriate edge-to-edge system bar styling for the current theme.
     *
     * @param darkTheme Whether the application is currently using a dark theme.
     */
    @Composable
    private fun ApplyEdgeToEdgeStyle(darkTheme: Boolean) {
        SideEffect {
            enableEdgeToEdge(
                statusBarStyle = systemBarStyle(darkTheme),
                navigationBarStyle = systemBarStyle(darkTheme),
            )
        }
    }

    /**
     * Creates the system bar style matching the current theme.
     *
     * @param darkTheme Whether the application is currently using a dark theme.
     * @return The appropriate [SystemBarStyle] for the status and navigation bars.
     */
    private fun systemBarStyle(darkTheme: Boolean): SystemBarStyle =
        if (darkTheme) {
            SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        } else {
            SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT,
            )
        }
}