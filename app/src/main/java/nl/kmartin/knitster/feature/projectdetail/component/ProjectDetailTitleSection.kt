package nl.kmartin.knitster.feature.projectdetail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.kmartin.knitster.data.model.ProjectIcon
import nl.kmartin.knitster.theme.KnitsterBorder
import nl.kmartin.knitster.theme.knitsterBorder
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
    projectIcon: ProjectIcon,
    onProjectIconClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FilledIconButton(
            modifier = Modifier
                .fillMaxHeight()
                .knitsterBorder()
                .aspectRatio(1f),
            onClick = onProjectIconClick,
            shape = KnitsterBorder.Shape,
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Icon(
                painter = painterResource(projectIcon.drawableRes),
                contentDescription = "Change project icon"
            )
        }
        KnitsterTextField(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            state = projectNameState,
            placeholder = "Project name",
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                capitalization = KeyboardCapitalization.Sentences,
            ),
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
        onProjectIconClick = {},
        projectIcon = ProjectIcon.DEFAULT
    )
}