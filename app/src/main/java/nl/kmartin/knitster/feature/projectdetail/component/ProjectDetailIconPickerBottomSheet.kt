package nl.kmartin.knitster.feature.projectdetail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import nl.kmartin.knitster.data.model.ProjectIcon
import nl.kmartin.knitster.theme.Dimensions
import nl.kmartin.knitster.theme.KnitsterBorder
import nl.kmartin.knitster.theme.knitsterBorder

/**
 * Displays a bottom sheet for selecting a project icon.
 *
 * @param onDismiss Called when the bottom sheet should be dismissed.
 * @param onIconSelected Called when the user selects a project icon.
 * @param currentIcon Currently selected project icon.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProjectDetailIconPickerBottomSheet(
    onDismiss: () -> Unit,
    onIconSelected: (ProjectIcon) -> Unit,
    currentIcon: ProjectIcon
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val hideSheetScope = rememberCoroutineScope()

    // Hides the bottom sheet before invoking [onHidden].
    fun hideSheet(onHidden: () -> Unit) {
        hideSheetScope.launch { sheetState.hide() }
            .invokeOnCompletion {
                if (!sheetState.isVisible) {
                    onHidden()
                }
            }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(Dimensions.ScreenPadding)
                .fillMaxWidth()
        ) {
            Text(
                text = "Choose project icon",
                style = MaterialTheme.typography.titleLarge
            )
            IconPickerGrid(
                selectedIcon = currentIcon,
                onIconSelected = { icon ->
                    onIconSelected(icon)
                    hideSheet(onHidden = onDismiss)
                }
            )
            TextButton(
                onClick = { hideSheet(onHidden = onDismiss) }
            ) {
                Text(
                    text = "Cancel",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

/**
 * Displays the grid of available project icons.
 *
 * @param selectedIcon Currently selected project icon.
 * @param onIconSelected Called when the user selects a project icon.
 */
@Composable
private fun IconPickerGrid(
    selectedIcon: ProjectIcon,
    onIconSelected: (ProjectIcon) -> Unit,
) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(
            space = 24.dp,
            alignment = Alignment.CenterHorizontally
        ),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        maxItemsInEachRow = 3
    ) {
        ProjectIcon.entries.forEach { icon ->
            IconPickerItem(
                icon = icon,
                isSelected = icon == selectedIcon,
                onClick = { onIconSelected(icon) }
            )
        }
    }
}

/**
 * Displays a selectable project icon within the picker grid.
 *
 * @param icon Project icon to display.
 * @param isSelected Whether this icon is currently selected.
 * @param onClick Called when the icon is selected.
 */
@Composable
private fun IconPickerItem(
    icon: ProjectIcon,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    FilledIconButton(
        modifier = Modifier
            .knitsterBorder(width = if (isSelected) KnitsterBorder.Width * 2 else KnitsterBorder.Width)
            .size(64.dp)
            .aspectRatio(1f),
        onClick = onClick,
        shape = KnitsterBorder.Shape,
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Icon(
            painter = painterResource(icon.drawableRes),
            contentDescription = null
        )
    }
}

// --- Preview ---
@Preview(showBackground = true)
@Composable
private fun ProjectDetailIconPickerBottomSheetPreview() {
    ProjectDetailIconPickerBottomSheet(
        onDismiss = {},
        onIconSelected = {},
        currentIcon = ProjectIcon.DEFAULT
    )
}