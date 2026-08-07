package nl.kmartin.knitster.feature.projectdetail.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import nl.kmartin.knitster.R

/**
 * Displays a confirmation dialog before permanently deleting a project.
 *
 * @param onConfirm Called when the user confirms the deletion.
 * @param onDismiss Called when the dialog is dismissed without deleting the project.
 * @param projectName Name of the project to display in the confirmation message.
 */
@Composable
internal fun ProjectDetailDeleteProjectDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    projectName: String
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(
                    R.string.delete_project_title,
                    projectName
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