package nl.kmartin.knitster.feature.projectdetail.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nl.kmartin.knitster.R
import nl.kmartin.knitster.data.model.RowCounter
import nl.kmartin.knitster.theme.KnitsterBorder
import nl.kmartin.knitster.theme.KnitsterShapes
import java.text.NumberFormat

/**
 * Displays the row counters for the current project along with controls for
 * managing them.
 *
 * @param modifier Modifier to be applied to the root layout.
 * @param onIncrementClick Called when a row counter is incremented.
 * @param onDecrementClick Called when a row counter is decremented.
 * @param onResetClick Called when a row counter is reset.
 * @param onEditClick Called when a row counter is edited.
 * @param onDeleteClick Called when a row counter is deleted.
 * @param onAddRowCounterClick Called when a new row counter should be added.
 * @param rowCounters Row counters to display.
 */
@Composable
internal fun ProjectDetailRowCounterSection(
    modifier: Modifier = Modifier,
    onIncrementClick: (Long) -> Unit,
    onDecrementClick: (Long) -> Unit,
    onResetClick: (Long) -> Unit,
    onEditClick: (Long) -> Unit,
    onDeleteClick: (Long) -> Unit,
    onAddRowCounterClick: () -> Unit,
    rowCounters: List<RowCounter>
) {
    val itemBackgroundColor = MaterialTheme.colorScheme.surfaceContainer
    val itemOnColor = MaterialTheme.colorScheme.onSurface
    val itemShape = KnitsterBorder.Shape

    Column(
        modifier = Modifier.fillMaxWidth()
            .animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Counters",
                style = MaterialTheme.typography.titleLarge
            )
            FilledTonalIconButton(
                onClick = onAddRowCounterClick
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = "Add"
                )
            }
        }

        rowCounters.forEach { rowCounter ->
            key(rowCounter.id) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = itemBackgroundColor,
                    contentColor = itemOnColor,
                    shape = itemShape
                ) {
                    RowCounterItem(
                        onIncrementClick = onIncrementClick,
                        onDecrementClick = onDecrementClick,
                        onResetClick = onResetClick,
                        onEditClick = onEditClick,
                        onDeleteClick = onDeleteClick,
                        rowCounter = rowCounter
                    )
                }
            }
        }
    }
}

/**
 * Displays a single row counter with controls for incrementing, decrementing,
 * and managing the counter.
 *
 * @param modifier Modifier to be applied to the root layout.
 * @param onIncrementClick Called when the counter is incremented.
 * @param onDecrementClick Called when the counter is decremented.
 * @param onResetClick Called when the counter is reset.
 * @param onEditClick Called when the counter is edited.
 * @param onDeleteClick Called when the counter is deleted.
 * @param rowCounter Row counter to display.
 */
@Composable
private fun RowCounterItem(
    modifier: Modifier = Modifier,
    onIncrementClick: (Long) -> Unit,
    onDecrementClick: (Long) -> Unit,
    onResetClick: (Long) -> Unit,
    onEditClick: (Long) -> Unit,
    onDeleteClick: (Long) -> Unit,
    rowCounter: RowCounter
) {
    val numberFormatter = remember { NumberFormat.getIntegerInstance() }
    val counterButtonSize = 72.dp

    Column(
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var menuExpanded by remember { mutableStateOf(false) }

        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = rowCounter.name,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 48.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                autoSize = TextAutoSize.StepBased(
                    minFontSize = MaterialTheme.typography.titleSmall.fontSize,
                    maxFontSize = MaterialTheme.typography.titleLarge.fontSize,
                    stepSize = 1.sp
                ),
            )

            Box(modifier = Modifier.align(Alignment.TopEnd)) {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_more_vert),
                        contentDescription = "Row counter options"
                    )
                }

                RowCounterItemOverFlowMenu(
                    onResetClick = { onResetClick(rowCounter.id) },
                    onEditClick = { onEditClick(rowCounter.id) },
                    onDeleteClick = { onDeleteClick(rowCounter.id) }
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CounterButton(
                modifier = Modifier.weight(1f),
                onClick = { onDecrementClick(rowCounter.id) },
                size = counterButtonSize,
                iconRes = R.drawable.ic_remove,
                contentDescription = "Decrement"
            )

            Text(
                text = buildString {
                    append(numberFormatter.format(rowCounter.count))

                    rowCounter.target?.let { target ->
                        append(" / ")
                        append(numberFormatter.format(target))
                    }
                },
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontFeatureSettings = "tnum"
                ),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                autoSize = TextAutoSize.StepBased(
                    minFontSize = MaterialTheme.typography.bodySmall.fontSize,
                    maxFontSize = MaterialTheme.typography.displayMedium.fontSize,
                    stepSize = 6.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            CounterButton(
                modifier = Modifier.weight(1f),
                onClick = { onIncrementClick(rowCounter.id) },
                size = counterButtonSize,
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
 * @param size Size of the button.
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

/**
 * Displays the overflow menu for a row counter.
 *
 * @param onResetClick Called when the Reset action is selected.
 * @param onEditClick Called when the Edit action is selected.
 * @param onDeleteClick Called when the Delete action is selected.
 */
@Composable
private fun RowCounterItemOverFlowMenu(
    onResetClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { showMenu = true }) {
            Icon(
                painter = painterResource(R.drawable.ic_more_vert),
                contentDescription = "More options"
            )
        }

        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false },
        ) {
            DropdownMenuItem(
                text = { Text("Reset Counter") },
                onClick = {
                    showMenu = false
                    onResetClick()
                },
            )
            DropdownMenuItem(
                text = { Text("Edit Counter") },
                onClick = {
                    showMenu = false
                    onEditClick()
                },
            )
            DropdownMenuItem(
                text = { Text("Delete Counter") },
                onClick = {
                    showMenu = false
                    onDeleteClick()
                },
            )
        }
    }
}

// --- Preview ---
@Preview(showBackground = true, heightDp = 200)
@Composable
private fun PreviewProjectDetailRowCounterSection() {
    ProjectDetailRowCounterSection(
        onIncrementClick = {},
        onDecrementClick = {},
        rowCounters = listOf(RowCounter(1, "Rows", 12)),
        onAddRowCounterClick = {},
        onResetClick = {},
        onEditClick = {},
        onDeleteClick = {}
    )
}