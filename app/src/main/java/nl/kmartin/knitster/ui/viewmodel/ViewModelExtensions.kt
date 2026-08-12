package nl.kmartin.knitster.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

/**
 * Launches an asynchronous operation in this [ViewModel]'s scope.
 *
 * If the operation completes successfully, its result is passed to [onSuccess].
 * If the operation throws an exception, the error is optionally logged and
 * [onError] is invoked. Coroutine cancellation is rethrown so normal
 * cancellation behavior is preserved.
 *
 * @param operation Suspended operation to execute.
 * @param onSuccess Called with the result when the operation completes successfully.
 * @param onError Called when the operation fails with an exception.
 * @param logTag Optional tag used when logging an error.
 * @param errorLogMsg Message used when logging an error.
 * @return The [Job] representing the launched operation.
 */
fun <T> ViewModel.launchOperation(
    operation: suspend () -> T,
    onSuccess: (T) -> Unit = {},
    onError: () -> Unit,
    logTag: String? = null,
    errorLogMsg: String = ""
): Job {
    return viewModelScope.launch {
        try {
            val result = operation()
            onSuccess(result)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            logTag?.let { Log.e(it, errorLogMsg, e) }
            onError()
        }
    }
}
