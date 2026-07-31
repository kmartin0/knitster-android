package nl.kmartin.knitster.feature.projectdetail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import nl.kmartin.knitster.R
import nl.kmartin.knitster.theme.KnitsterBorder
import nl.kmartin.knitster.theme.KnitsterShapes

/**
 * Height of the increment and decrement buttons in the row counter.
 */
private val CounterButtonHeight = 48.dp

/**
 * Displays a row counter with increment and decrement controls.
 *
 * @param modifier Modifier to be applied to the root layout.
 * @param onIncrementClick Called when the increment button is clicked.
 * @param onDecrementClick Called when the decrement button is clicked.
 * @param rowCount The current row count to display.
 */
@Composable
internal fun ProjectDetailRowCounter(
    modifier: Modifier = Modifier,
    compact: Boolean = false,
    onIncrementClick: () -> Unit,
    onDecrementClick: () -> Unit,
    rowCount: Int
) {
    val counterTextStyle = if (compact) {
        MaterialTheme.typography.displaySmall
    } else {
        MaterialTheme.typography.displayMedium
    }

    val counterButtonSize = if (compact) 48.dp else 84.dp

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(KnitsterBorder.Shape),
        color = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        Column(
            modifier = Modifier.padding(if(compact) 12.dp else 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!compact) {
                Text(
                    text = "Row Counter",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CounterButton(
                    modifier = Modifier.weight(1f),
                    onClick = onDecrementClick,
                    size = counterButtonSize,
                    iconRes = R.drawable.ic_remove,
                    contentDescription = "Decrement"
                )

                Text(
                    text = "$rowCount",
                    style = counterTextStyle.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                CounterButton(
                    modifier = Modifier.weight(1f),
                    onClick = onIncrementClick,
                    size = counterButtonSize,
                    iconRes = R.drawable.ic_add,
                    contentDescription = "Increment"
                )
            }
        }
    }
}

/**
 * Displays a counter button with the specified icon.
 *
 * @param modifier Modifier to be applied to the root layout.
 * @param onClick Called when the button is clicked.
 * @param iconRes Drawable resource for the button icon.
 * @param contentDescription Content description for accessibility.
 */
@Composable
private fun CounterButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    iconRes: Int,
    size: Dp,
    contentDescription: String,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        FilledIconButton(
            modifier = Modifier
                .size(size)
                .aspectRatio(1f),
            onClick = onClick,
            shape = KnitsterShapes.large
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = contentDescription
            )
        }
    }
}

// --- Preview ---
@Preview(showBackground = true, heightDp = 200)
@Composable
private fun PreviewProjectDetailRowCounter() {
    ProjectDetailRowCounter(
        onIncrementClick = {},
        onDecrementClick = {},
        rowCount = 12
    )
}