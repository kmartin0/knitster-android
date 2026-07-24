package nl.kmartin.knitster.feature.projectdetail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.kmartin.knitster.R
import nl.kmartin.knitster.ui.component.KnitsterIconButton
import nl.kmartin.knitster.ui.component.KnitsterTextField

/**
 * Displays the project icon button and editable project name.
 *
 * @param modifier Modifier to be applied to the root layout.
 * @param projectNameState State backing the project name text field.
 * @param onProjectIconClick Called when the project icon button is clicked.
 */
@Composable
internal fun ProjectDetailTitleSection(
    modifier: Modifier = Modifier,
    projectNameState: TextFieldState,
    onProjectIconClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        KnitsterIconButton(
            modifier = Modifier.fillMaxHeight(),
            onClick = onProjectIconClick,
            iconRes = R.drawable.ic_sweater,
            contentDescription = "Change project icon",
            iconSize = Modifier.fillMaxSize(0.8f)
        )
        KnitsterTextField(
            modifier = Modifier.weight(1f),
            state = projectNameState,
            placeholder = "Project name",
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            onKeyboardAction = { focusManager.clearFocus() },
            textStyle = MaterialTheme.typography.titleLarge
        )
    }
}

// --- Preview ---
@Preview(showBackground = true)
@Composable
private fun ProjectDetailTitleSectionPreview() {
    ProjectDetailTitleSection(
        projectNameState = TextFieldState(initialText = "Hello World"),
        onProjectIconClick = {}
    )
}