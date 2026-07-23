package nl.kmartin.knitster.feature.projectdetail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.kmartin.knitster.R
import nl.kmartin.knitster.ui.component.KnitsterIconButton

/**
 * Height of the increment and decrement buttons in the row counter.
 */
private val CounterButtonHeight = 84.dp

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
    onIncrementClick: () -> Unit,
    onDecrementClick: () -> Unit,
    rowCount: Int
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Row Counter",
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CounterButton(
                modifier = Modifier.weight(1f),
                onClick = onDecrementClick,
                iconRes = R.drawable.ic_remove,
                contentDescription = "Decrement"
            )

            Text(
                text = "$rowCount",
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            CounterButton(
                modifier = Modifier.weight(1f),
                onClick = onIncrementClick,
                iconRes = R.drawable.ic_add,
                contentDescription = "Increment"
            )
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
    contentDescription: String,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        KnitsterIconButton(
            modifier = Modifier.height(CounterButtonHeight),
            iconSize = Modifier.fillMaxSize(0.5f),
            onClick = onClick,
            iconRes = iconRes,
            contentDescription = contentDescription,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewProjectDetailRowCounter() {
    ProjectDetailRowCounter(
        onIncrementClick = {},
        onDecrementClick = {},
        rowCount = 12
    )
}