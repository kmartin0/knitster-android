package nl.kmartin.knitster.feature.projectdetail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.kmartin.knitster.ui.component.KnitsterTextField

/**
 * Displays a multi-line text field for editing the project's notes.
 *
 * @param modifier Modifier to be applied to the root layout.
 * @param projectNotesState State backing the notes text field.
 */
@Composable
internal fun ProjectDetailNotes(
    modifier: Modifier = Modifier,
    projectNotesState: TextFieldState
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = "Notes",
            style = MaterialTheme.typography.titleMedium,
        )
        KnitsterTextField(
            modifier = Modifier.fillMaxSize(),
            state = projectNotesState,
            lineLimits = TextFieldLineLimits.MultiLine(),
        )
    }
}

// --- Preview ---
@Preview(showBackground = true, heightDp = 200)
@Composable
private fun ProjectDetailNotesPreview() {
    ProjectDetailNotes(
        projectNotesState = TextFieldState(initialText = "Hello World")
    )
}