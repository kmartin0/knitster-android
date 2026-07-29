package nl.kmartin.knitster.feature.projectlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.visible
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.kmartin.knitster.R
import nl.kmartin.knitster.theme.Dimensions
import nl.kmartin.knitster.theme.KnitsterBorder
import nl.kmartin.knitster.theme.ThemeColor
import nl.kmartin.knitster.theme.ThemeMode
import nl.kmartin.knitster.theme.colorSchemeFor
import nl.kmartin.knitster.theme.knitsterBorder
import nl.kmartin.knitster.theme.resolveDarkTheme

/**
 * Displays a bottom sheet that allows the user to customize the application's
 * appearance.
 *
 * Users can choose both the color theme and brightness mode.
 *
 * @param currentThemeColor Currently selected color theme.
 * @param currentThemeMode Currently selected brightness mode.
 * @param onThemeColorSelected Called when a color theme is selected.
 * @param onThemeModeSelected Called when a brightness mode is selected.
 * @param onDismiss Called when the bottom sheet should be dismissed.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectListSettingsBottomSheet(
    currentThemeColor: ThemeColor,
    currentThemeMode: ThemeMode,
    onThemeColorSelected: (ThemeColor) -> Unit,
    onThemeModeSelected: (ThemeMode) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier.padding(Dimensions.ScreenPadding),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            ThemeColorPickerGrid(
                currentThemeColor = currentThemeColor,
                currentThemeMode = currentThemeMode,
                onThemeColorSelected = onThemeColorSelected
            )
            ThemeModeGrid(
                currentThemeMode = currentThemeMode,
                onThemeModeSelected = onThemeModeSelected
            )
        }
    }
}

/**
 * Displays the available color themes.
 *
 * The currently selected theme is highlighted and selecting a theme invokes
 * [onThemeColorSelected].
 *
 * @param currentThemeColor Currently selected color theme.
 * @param currentThemeMode Currently selected brightness mode, used to preview
 * the color themes in either light or dark mode.
 * @param onThemeColorSelected Called when a color theme is selected.
 */
@Composable
private fun ThemeColorPickerGrid(
    currentThemeColor: ThemeColor,
    currentThemeMode: ThemeMode,
    onThemeColorSelected: (ThemeColor) -> Unit,
) {
    val isDarkMode = currentThemeMode.resolveDarkTheme(isSystemInDarkTheme())
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Colour Theme",
            style = MaterialTheme.typography.titleLarge
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            ThemeColor.entries.forEach { themeColor ->
                ThemeColorItem(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    themeColor = themeColor,
                    isDarkMode = isDarkMode,
                    isSelected = themeColor == currentThemeColor,
                    onClick = { onThemeColorSelected(themeColor) }
                )
            }
        }
    }
}

/**
 * Displays a selectable color theme preview.
 *
 * @param themeColor color theme represented by this item.
 * @param isDarkMode Whether the preview should be rendered using the dark color scheme.
 * @param isSelected Whether this color theme is currently selected.
 * @param onClick Called when the item is selected.
 * @param modifier Modifier to apply to the item.
 */
@Composable
private fun ThemeColorItem(
    themeColor: ThemeColor,
    isDarkMode: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val previewScheme = remember(themeColor, isDarkMode) {
        colorSchemeFor(themeColor, isDarkMode = isDarkMode)
    }
    Column(
        modifier = modifier
            .knitsterBorder(
                color = previewScheme.secondary,
                width = if (isSelected) KnitsterBorder.Width * 2 else KnitsterBorder.Width
            )
            .clickable(
                onClick = onClick,
                role = Role.RadioButton,
                onClickLabel = themeColor.displayName,
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
            )
            .background(color = previewScheme.primaryContainer)
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier
                .align(Alignment.End)
                .visible(isSelected),
            painter = painterResource(R.drawable.ic_check_circle),
            contentDescription = null,
            tint = previewScheme.onPrimaryContainer,
        )
        Icon(
            modifier = Modifier
                .fillMaxWidth(1f)
                .aspectRatio(1f),
            painter = painterResource(R.drawable.ic_knitting_sketch),
            contentDescription = null,
            tint = previewScheme.onPrimaryContainer
        )
        Text(
            text = themeColor.displayName,
            color = previewScheme.onPrimaryContainer,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

/**
 * Displays the available brightness modes.
 *
 * The currently selected mode is highlighted and selecting a mode invokes
 * [onThemeModeSelected].
 *
 * @param currentThemeMode Currently selected brightness mode.
 * @param onThemeModeSelected Called when a brightness mode is selected.
 */
@Composable
private fun ThemeModeGrid(
    currentThemeMode: ThemeMode,
    onThemeModeSelected: (ThemeMode) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Brightness",
            style = MaterialTheme.typography.titleLarge
        )
        ThemeMode.entries.forEach { themeMode ->
            ThemeModeItem(
                themeMode = themeMode,
                isSelected = themeMode == currentThemeMode,
                onClick = { onThemeModeSelected(themeMode) }
            )
        }
    }
}

/**
 * Displays a selectable brightness mode.
 *
 * @param modifier Modifier to apply to the item.
 * @param themeMode Brightness mode represented by this item.
 * @param isSelected Whether this brightness mode is currently selected.
 * @param onClick Called when the item is selected.
 */
@Composable
private fun ThemeModeItem(
    modifier: Modifier = Modifier,
    themeMode: ThemeMode,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .knitsterBorder(
                color = MaterialTheme.colorScheme.primary,
                width = if (isSelected) KnitsterBorder.Width * 2 else KnitsterBorder.Width
            )
            .background(color = MaterialTheme.colorScheme.surfaceContainerHighest)
            .padding(8.dp)
            .clickable(
                onClick = onClick,
                role = Role.RadioButton,
                onClickLabel = themeMode.displayName,
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            painter = painterResource(themeMode.drawableRes),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = themeMode.displayName,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            modifier = Modifier
                .visible(isSelected),
            painter = painterResource(R.drawable.ic_check_circle),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

// --- Preview ---
@Preview(showBackground = true)
@Composable
private fun ProjectListSettingsBottomSheetPreview() {
    ProjectListSettingsBottomSheet(
        currentThemeColor = ThemeColor.DEFAULT,
        currentThemeMode = ThemeMode.DEFAULT,
        onThemeModeSelected = {},
        onThemeColorSelected = {},
        onDismiss = {}
    )
}
