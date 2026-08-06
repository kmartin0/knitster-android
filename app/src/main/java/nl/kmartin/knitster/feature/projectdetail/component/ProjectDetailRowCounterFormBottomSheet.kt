package nl.kmartin.knitster.feature.projectdetail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import nl.kmartin.knitster.data.model.RowCounter
import nl.kmartin.knitster.theme.Dimensions

/**
 * Displays a bottom sheet for creating or editing a row counter.
 *
 * @param title Title displayed at the top of the form.
 * @param rowCounter Initial row-counter values shown in the form.
 * @param onDismiss Called when the bottom sheet is dismissed.
 * @param onSave Called with the updated row counter when the user saves the form.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProjectDetailRowCounterFormBottomSheet(
    title: String,
    rowCounter: RowCounter,
    onDismiss: () -> Unit,
    onSave: (RowCounter) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scope = rememberCoroutineScope()

    // Hide the sheet before invoking the supplied callback.
    fun hideSheet(onHidden: () -> Unit) {
        scope.launch {
            sheetState.hide()
        }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                onHidden()
            }
        }
    }

    // Override the default content window insets and apply system bar padding manually.
    // This prevents a bounce loop that can occur when the bottom sheet reaches
    // the top of the screen and is dragged further upward.
    ModalBottomSheet(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        contentWindowInsets = { WindowInsets(0.dp) }
    ) {
        RowCounterForm(
            title = title,
            rowCounter = rowCounter,
            onSave = { updatedRowCounter ->
                hideSheet {
                    onSave(updatedRowCounter)
                }
            },
            onCancel = {
                hideSheet(onDismiss)
            }
        )
    }
}

/**
 * Displays a form for editing the properties of a row counter.
 *
 * The form maintains its own editable field state and returns an updated
 * [RowCounter] when the user saves.
 *
 * @param rowCounter Initial values displayed in the form.
 * @param title Title displayed at the top of the form.
 * @param onSave Called with the updated row counter when the user saves.
 * @param onCancel Called when the user cancels editing.
 * @param modifier Modifier applied to the form container.
 */
@Composable
private fun RowCounterForm(
    rowCounter: RowCounter,
    title: String,
    onSave: (RowCounter) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    val nameState = rememberTextFieldState(
        initialText = rowCounter.name
    )

    val countState = rememberTextFieldState(
        initialText = rowCounter.count
            .toString()
    )

    val targetState = rememberTextFieldState(
        initialText = rowCounter.target
            ?.toString()
            .orEmpty()
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 360.dp)
                .fillMaxWidth()
                .padding(Dimensions.ScreenPadding),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RowCounterFormTitle(title = title)
            RowCounterNameField(state = nameState)
            RowCounterCountField(state = countState)
            RowCounterTargetField(state = targetState)
            RowCounterFormActions(
                onSaveClick = {
                    onSave(
                        rowCounter.copy(
                            name = nameState.text.toString(),
                            count = countState.text
                                .toString()
                                .toIntOrNull() ?: 0,
                            target = targetState.text
                                .toString()
                                .toIntOrNull()
                        )
                    )
                },
                onCancelClick = onCancel
            )
        }
    }
}

/**
 * Displays the title of the row-counter form.
 *
 * @param modifier Modifier applied to the title.
 * @param title Title to display.
 */
@Composable
private fun RowCounterFormTitle(
    modifier: Modifier = Modifier,
    title: String
) {
    Text(
        modifier = modifier,
        text = title,
        style = MaterialTheme.typography.titleLarge
    )
}

/**
 * Displays the row-counter name input field.
 *
 * @param state State backing the text field.
 * @param modifier Modifier applied to the text field.
 */
@Composable
private fun RowCounterNameField(
    state: TextFieldState,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        state = state,
        label = {
            Text("Name")
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            capitalization = KeyboardCapitalization.Sentences,
        ),
        lineLimits = TextFieldLineLimits.SingleLine
    )
}

/**
 * Displays the row-counter count input field.
 *
 * The field accepts numeric input only.
 *
 * @param state State backing the text field.
 * @param modifier Modifier applied to the text field.
 */
@Composable
private fun RowCounterCountField(
    state: TextFieldState,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        state = state,
        label = { Text("Count") },
        placeholder = { Text("0") },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        inputTransformation = InputTransformation {
            if (!asCharSequence().all(Char::isDigit)) {
                revertAllChanges()
            }
        },
        lineLimits = TextFieldLineLimits.SingleLine
    )
}

/**
 * Displays the optional row-counter target input field.
 *
 * The field accepts numeric input only.
 *
 * @param state State backing the text field.
 * @param modifier Modifier applied to the text field.
 */
@Composable
private fun RowCounterTargetField(
    state: TextFieldState,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        state = state,
        label = {
            Text("Target (optional)")
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        inputTransformation = InputTransformation {
            if (!asCharSequence().all(Char::isDigit)) {
                revertAllChanges()
            }
        },
        lineLimits = TextFieldLineLimits.SingleLine
    )
}

/**
 * Displays the save and cancel actions for the row-counter form.
 *
 * @param onSaveClick Called when the Save button is clicked.
 * @param onCancelClick Called when the Cancel button is clicked.
 * @param modifier Modifier applied to the action container.
 */
@Composable
private fun RowCounterFormActions(
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onSaveClick
        ) {
            Text(
                text = "Save",
                style = MaterialTheme.typography.titleMedium
            )
        }

        TextButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onCancelClick
        ) {
            Text(
                text = "Cancel",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

// --- preview ---
@Preview(showBackground = true)
@Composable
private fun RowCounterFormPreview() {
    RowCounterForm(
        rowCounter = RowCounter(),
        title = "Create Row",
        onSave = {},
        onCancel = {}
    )
}
