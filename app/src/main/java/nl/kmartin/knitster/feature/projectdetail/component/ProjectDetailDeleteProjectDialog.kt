package nl.kmartin.knitster.feature.projectdetail.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle

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
                text = buildAnnotatedString {
                    append("Delete ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Medium)) {
                        append(projectName)
                    }
                    append("?")
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        text = { Text("This action cannot be undone.") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}