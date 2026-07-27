package nl.kmartin.knitster.feature.projectdetail.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import nl.kmartin.knitster.data.model.ProjectIcon
import nl.kmartin.knitster.theme.KnitsterBorder
import nl.kmartin.knitster.theme.knitsterBorder
import nl.kmartin.knitster.ui.component.KnitsterIconButton

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
    val sheetState = rememberModalBottomSheetState()
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
        sheetState = sheetState,
    ) {
        IconPickerGrid(
            selectedIcon = currentIcon,
            onIconSelected = { icon ->
                onIconSelected(icon)
                hideSheet(onHidden = onDismiss)
            }
        )
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
    LazyVerticalGrid(
        columns = GridCells.Fixed(3), // 3 columns, matching your mockup
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(16.dp),
    ) {
        items(
            items = ProjectIcon.entries,
            key = { it.id },
        ) { icon ->
            IconPickerItem(
                icon = icon,
                isSelected = icon == selectedIcon,
                onClick = { onIconSelected(icon) },
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
    Column(
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onClick)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        KnitsterIconButton(
            modifier = Modifier
                .size(64.dp),
            onClick = onClick,
            iconRes = icon.drawableRes,
            contentDescription = icon.id,
            border = Modifier.knitsterBorder(
                width = if (isSelected) KnitsterBorder.Width * 2 else KnitsterBorder.Width
            ),
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