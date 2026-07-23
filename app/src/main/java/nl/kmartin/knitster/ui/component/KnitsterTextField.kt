package nl.kmartin.knitster.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import nl.kmartin.knitster.theme.knitsterBorder

/**
 * Displays a text field using the app's default styling.
 *
 * @param modifier Modifier to be applied to the text field.
 * @param state State backing the text field.
 * @param placeholder Placeholder text displayed when the field is empty.
 * @param lineLimits Controls the allowed number of text lines.
 * @param keyboardOptions Software keyboard configuration.
 * @param onKeyboardAction Called when a keyboard action is performed.
 * @param textStyle Style applied to the entered text.
 * @param textColor Color of the entered text.
 * @param placeholderColor Color of the placeholder text.
 */
@Composable
fun KnitsterTextField(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    placeholder: String = "",
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.SingleLine,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActionHandler? = null,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    placeholderColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    val isSingleLine = lineLimits == TextFieldLineLimits.SingleLine
    val boxAlignment = if (isSingleLine) Alignment.CenterStart else Alignment.TopStart

    BasicTextField(
        state = state,
        modifier = modifier
            .fillMaxWidth()
            .knitsterBorder()
            .then(if (isSingleLine) Modifier.heightIn(min = 64.dp) else Modifier),
        lineLimits = lineLimits,
        keyboardOptions = keyboardOptions,
        onKeyboardAction = onKeyboardAction,
        textStyle = textStyle.copy(color = textColor),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        decorator = { innerTextField ->
            Box(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
                contentAlignment = boxAlignment,
            ) {
                if (state.text.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = textStyle.copy(color = placeholderColor),
                    )
                }
                innerTextField()
            }
        },
    )
}