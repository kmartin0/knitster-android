package nl.kmartin.knitster.ui.component

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

/**
 * Observes a pending error message and displays it in a snackbar.
 *
 * @param errorMessage Error message to display, or `null` if no error is pending.
 * @param snackbarHostState State used to display the snackbar.
 * @param onErrorShown Called after the pending error has been consumed.
 */
@Composable
fun ObserveSnackbarError(
    errorMessage: String?,
    snackbarHostState: SnackbarHostState,
    onErrorShown: () -> Unit,
) {
    // Remember the scope locally so snackbar display isn't canceled when the effect restarts.
    val snackBarScope = rememberCoroutineScope()

    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            onErrorShown()

            snackBarScope.launch {
                snackbarHostState.currentSnackbarData?.dismiss()
                snackbarHostState.showSnackbar(message)
            }
        }
    }
}