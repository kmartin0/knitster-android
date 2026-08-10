package nl.kmartin.knitster.main

import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import nl.kmartin.knitster.data.model.resolveDarkTheme
import nl.kmartin.knitster.navigation.AppNavigation
import nl.kmartin.knitster.theme.KnitsterTheme
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainActivityViewModel by viewModels()

    /**
     * Initializes the activity and its Compose UI.
     *
     * Configures the splash screen and creates the root Compose hierarchy using
     * the current application settings.
     *
     * @param savedInstanceState Previously saved activity state, or `null` when the
     * activity is being created for the first time.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        configureSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            MainActivityContent(
                uiState = uiState
            )
        }
    }

    /**
     * Configures the splash screen to remain visible until the application settings
     * are loaded and the minimum display duration has elapsed.
     */
    private fun configureSplashScreen() {
        val splashScreen = installSplashScreen()
        val isMinDurationElapsed = AtomicBoolean(false)

        splashScreen.setKeepOnScreenCondition {
            !isMinDurationElapsed.get() ||
                    viewModel.uiState.value.appSettings == null
        }

        lifecycleScope.launch {
            delay(2.seconds)
            isMinDurationElapsed.set(true)
        }
    }

    /**
     * Displays the root application content using the current application settings.
     *
     * Applies system UI configuration before creating the themed navigation hierarchy.
     *
     * @param uiState Current activity UI state.
     */
    @Composable
    private fun MainActivityContent(
        uiState: MainActivityUiState
    ) {
        uiState.appSettings?.let { loadedAppSettings ->
            val darkTheme = loadedAppSettings.themeMode.resolveDarkTheme(
                isSystemInDarkTheme()
            )

            ApplyEdgeToEdgeStyle(darkTheme)
            ApplyKeepScreenAwake(loadedAppSettings.keepScreenAwake)

            KnitsterTheme(
                themeColor = loadedAppSettings.themeColor,
                themeMode = loadedAppSettings.themeMode
            ) {
                AppNavigation()
            }
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
     * Applies the screen-awake window behavior for the current setting.
     *
     * @param keepScreenAwake Whether to prevent the screen from turning off.
     */
    @Composable
    private fun ApplyKeepScreenAwake(keepScreenAwake: Boolean) {
        SideEffect {
            if (keepScreenAwake) {
                window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }
    }

    /**
     * Creates the system bar style matching the current theme.
     *
     * @param darkTheme Whether the application is currently using a dark theme.
     * @return The appropriate [SystemBarStyle] for the status and navigation bars.
     */
    private fun systemBarStyle(darkTheme: Boolean): SystemBarStyle {
        return if (darkTheme) {
            SystemBarStyle.dark(Color.TRANSPARENT)
        } else {
            SystemBarStyle.light(
                Color.TRANSPARENT,
                Color.TRANSPARENT,
            )
        }
    }
}