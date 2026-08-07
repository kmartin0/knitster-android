package nl.kmartin.knitster.feature.projectdetail.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import nl.kmartin.knitster.R
import nl.kmartin.knitster.data.model.RowCounter

/**
 * Displays a confirmation dialog before permanently deleting a row counter.
 *
 * @param onConfirm Called when the user confirms the deletion.
 * @param onDismiss Called when the dialog is dismissed without deleting the row counter.
 * @param rowCounter Row counter to display in the confirmation message.
 */
@Composable
internal fun ProjectDetailDeleteRowCounterDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    rowCounter: RowCounter
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(
                    R.string.delete_row_counter_title,
                    rowCounter.name,
                    rowCounter.count
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        text = { Text(stringResource(R.string.action_cannot_be_undone)) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.delete))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}