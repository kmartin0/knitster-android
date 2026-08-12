package nl.kmartin.knitster.feature.projectdetail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nl.kmartin.knitster.R
import nl.kmartin.knitster.data.model.RowCounter
import nl.kmartin.knitster.theme.FontFeatures
import nl.kmartin.knitster.theme.KnitsterBorder
import nl.kmartin.knitster.theme.KnitsterShapes
import java.text.NumberFormat

private val rowCounterButtonSize = 72.dp

/**
 * Adds the row counter section to a lazy list.
 *
 * @param onIncrementClick Called when a row counter is incremented.
 * @param onDecrementClick Called when a row counter is decremented.
 * @param onMoveUpClick Called when the Move Up action is selected.
 * @param onMoveDownClick Called when the Move Down action is selected.
 * @param onResetClick Called when a row counter is reset.
 * @param onEditClick Called when a row counter is edited.
 * @param onDeleteClick Called when a row counter is deleted.
 * @param onAddRowCounterClick Called when a new row counter should be added.
 * @param rowCounters Row counters to display.
 */
internal fun LazyListScope.projectDetailRowCounterSection(
    onIncrementClick: (Long) -> Unit,
    onDecrementClick: (Long) -> Unit,
    onMoveUpClick: (Long) -> Unit,
    onMoveDownClick: (Long) -> Unit,
    onResetClick: (Long) -> Unit,
    onEditClick: (Long) -> Unit,
    onDeleteClick: (Long) -> Unit,
    onAddRowCounterClick: () -> Unit,
    rowCounters: List<RowCounter>
) {
    item {
        ProjectDetailRowCounterHeader(
            onAddRowCounterClick = onAddRowCounterClick
        )
    }

    items(
        items = rowCounters,
        key = { it.id }
    ) { rowCounter ->
        RowCounterItem(
            modifier = Modifier.animateItem(),
            onIncrementClick = onIncrementClick,
            onDecrementClick = onDecrementClick,
            onMoveUpClick = onMoveUpClick,
            onMoveDownClick = onMoveDownClick,
            onResetClick = onResetClick,
            onEditClick = onEditClick,
            onDeleteClick = onDeleteClick,
            rowCounter = rowCounter
        )
    }
}

/**
 * Displays the row counter section header and add button.
 *
 * @param modifier Modifier to be applied to the root layout.
 * @param onAddRowCounterClick Called when a new row counter should be added.
 */
@Composable
private fun ProjectDetailRowCounterHeader(
    modifier: Modifier = Modifier,
    onAddRowCounterClick: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.counters),
            style = MaterialTheme.typography.titleLarge
        )

        FilledTonalIconButton(
            onClick = onAddRowCounterClick
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_plus),
                contentDescription = stringResource(R.string.add_counter)
            )
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
 * @param onMoveUpClick Called when the Move Up action is selected.
 * @param onMoveDownClick Called when the Move Down action is selected.
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
    onMoveUpClick: (Long) -> Unit,
    onMoveDownClick: (Long) -> Unit,
    onResetClick: (Long) -> Unit,
    onEditClick: (Long) -> Unit,
    onDeleteClick: (Long) -> Unit,
    rowCounter: RowCounter
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = KnitsterBorder.Shape
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RowCounterItemTitle(
                title = rowCounter.name,
                onMoveUpClick = { onMoveUpClick(rowCounter.id) },
                onMoveDownClick = { onMoveDownClick(rowCounter.id) },
                onResetClick = { onResetClick(rowCounter.id) },
                onEditClick = { onEditClick(rowCounter.id) },
                onDeleteClick = { onDeleteClick(rowCounter.id) }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CounterButton(
                    modifier = Modifier.weight(1f),
                    onClick = { onDecrementClick(rowCounter.id) },
                    size = rowCounterButtonSize,
                    iconRes = R.drawable.ic_minus,
                    contentDescription = stringResource(R.string.decrement_counter)
                )

                RowCounterItemCount(
                    modifier = Modifier.weight(1f),
                    count = rowCounter.count,
                    target = rowCounter.target
                )

                CounterButton(
                    modifier = Modifier.weight(1f),
                    onClick = { onIncrementClick(rowCounter.id) },
                    size = rowCounterButtonSize,
                    iconRes = R.drawable.ic_plus,
                    contentDescription = stringResource(R.string.increment_counter)
                )
            }
        }
    }
}

/**
 * Displays the title and overflow actions for a row counter.
 *
 * @param modifier Modifier to be applied to the root layout.
 * @param title Title of the row counter.
 * @param onMoveUpClick Called when the Move Up action is selected.
 * @param onMoveDownClick Called when the Move Down action is selected.
 * @param onResetClick Called when the Reset action is selected.
 * @param onEditClick Called when the Edit action is selected.
 * @param onDeleteClick Called when the Delete action is selected.
 */
@Composable
private fun RowCounterItemTitle(
    modifier: Modifier = Modifier,
    title: String,
    onMoveUpClick: () -> Unit,
    onMoveDownClick: () -> Unit,
    onResetClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
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
            RowCounterItemOverFlowMenu(
                onMoveUpClick = onMoveUpClick,
                onMoveDownClick = onMoveDownClick,
                onResetClick = onResetClick,
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick
            )
        }
    }
}

/**
 * Displays the current row count and optional target count.
 *
 * @param modifier Modifier to be applied to the text.
 * @param count Current row count.
 * @param target Optional target row count.
 */
@Composable
private fun RowCounterItemCount(
    modifier: Modifier = Modifier,
    count: Int,
    target: Int?
) {
    val numberFormatter = remember { NumberFormat.getIntegerInstance() }

    Text(
        modifier = modifier,
        text = buildString {
            append(numberFormatter.format(count))

            target?.let {
                append(" / ")
                append(numberFormatter.format(it))
            }
        },
        style = MaterialTheme.typography.displayMedium.copy(
            fontWeight = FontWeight.Bold,
            fontFeatureSettings = FontFeatures.TABULAR_NUMS
        ),
        textAlign = TextAlign.Center,
        autoSize = TextAutoSize.StepBased(
            minFontSize = MaterialTheme.typography.bodySmall.fontSize,
            maxFontSize = MaterialTheme.typography.displayMedium.fontSize,
            stepSize = 6.sp
        ),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
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
 * @param onMoveUpClick Called when the Move Up action is selected.
 * @param onMoveDownClick Called when the Move Down action is selected.
 * @param onResetClick Called when the Reset action is selected.
 * @param onEditClick Called when the Edit action is selected.
 * @param onDeleteClick Called when the Delete action is selected.
 */
@Composable
private fun RowCounterItemOverFlowMenu(
    onMoveUpClick: () -> Unit,
    onMoveDownClick: () -> Unit,
    onResetClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { showMenu = true }) {
            Icon(
                painter = painterResource(R.drawable.ic_more_vert),
                contentDescription = stringResource(R.string.more_options)
            )
        }

        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false },
        ) {
            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_up),
                        contentDescription = null
                    )
                },
                text = { Text(stringResource(R.string.move_counter_up)) },
                onClick = {
                    showMenu = false
                    onMoveUpClick()
                },
            )

            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_down),
                        contentDescription = null
                    )
                },
                text = { Text(stringResource(R.string.move_counter_down)) },
                onClick = {
                    showMenu = false
                    onMoveDownClick()
                },
            )

            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_reset),
                        contentDescription = null
                    )
                },
                text = { Text(stringResource(R.string.reset_counter)) },
                onClick = {
                    showMenu = false
                    onResetClick()
                },
            )

            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_edit),
                        contentDescription = null
                    )
                },
                text = { Text(stringResource(R.string.edit_counter)) },
                onClick = {
                    showMenu = false
                    onEditClick()
                },
            )

            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_delete),
                        contentDescription = null
                    )
                },
                text = { Text(stringResource(R.string.delete_counter)) },
                onClick = {
                    showMenu = false
                    onDeleteClick()
                },
            )
        }
    }
}