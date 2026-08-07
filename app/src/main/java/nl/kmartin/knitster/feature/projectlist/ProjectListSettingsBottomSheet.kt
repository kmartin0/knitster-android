package nl.kmartin.knitster.feature.projectlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.visible
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import nl.kmartin.knitster.R
import nl.kmartin.knitster.theme.Dimensions
import nl.kmartin.knitster.theme.KnitsterBorder
import nl.kmartin.knitster.theme.ThemeColor
import nl.kmartin.knitster.theme.ThemeMode
import nl.kmartin.knitster.theme.colorSchemeFor
import nl.kmartin.knitster.theme.knitsterBorder
import nl.kmartin.knitster.theme.resolveDarkTheme

/** Minimum number of columns in the theme color grid. */
private const val themeColorGridMinColumns = 4

/** Maximum width of a theme color grid item. */
private val themeColorGridMaxItemWidth = 92.dp

/** Spacing between theme color grid items. */
private val themeColorGridItemSpacing = 8.dp

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
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        contentWindowInsets = { WindowInsets(0.dp) }
    ) {
        Column(
            modifier = Modifier
                .padding(Dimensions.ScreenPadding)
                .verticalScroll(rememberScrollState()),
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
            text = stringResource(R.string.settings_colour_theme),
            style = MaterialTheme.typography.titleLarge
        )
        ThemeColorGrid(
            themeColors = ThemeColor.entries,
            currentThemeColor = currentThemeColor,
            isDarkMode = isDarkMode,
            onThemeColorSelected = onThemeColorSelected
        )
    }
}

/**
 * Displays the available color themes in a responsive, non-lazy grid.
 *
 * The grid is at least [themeColorGridMinColumns] columns wide and adds
 * additional columns as the available width increases. Each item grows to
 * fill the available space but never exceeds [themeColorGridMaxItemWidth].
 *
 * @param themeColors Theme colors to display.
 * @param currentThemeColor Currently selected color theme.
 * @param isDarkMode Whether items should be previewed using the dark color scheme.
 * @param onThemeColorSelected Called when a color theme is selected.
 */
@Composable
private fun ThemeColorGrid(
    themeColors: List<ThemeColor>,
    currentThemeColor: ThemeColor,
    isDarkMode: Boolean,
    onThemeColorSelected: (ThemeColor) -> Unit
) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val columns = themeColorGridColumnsFor(maxWidth)
        val itemWidth = themeColorGridItemWidthFor(maxWidth, columns)

        Column(verticalArrangement = Arrangement.spacedBy(themeColorGridItemSpacing)) {
            themeColors.chunked(columns).forEach { rowThemeColors ->
                Row(
                    modifier = Modifier.height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.spacedBy(themeColorGridItemSpacing),
                ) {
                    rowThemeColors.forEach { themeColor ->
                        ThemeColorItem(
                            modifier = Modifier
                                .width(itemWidth)
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
    }
}

/**
 * Returns the number of columns that fit within the available width.
 *
 * The grid is guaranteed to contain at least [themeColorGridMinColumns] columns.
 * Additional columns are added whenever another target-sized item can fit.
 *
 * @param availableWidth Width available to the grid.
 * @return Number of columns to display.
 */
private fun themeColorGridColumnsFor(
    availableWidth: Dp
): Int {
    val columnWidth = themeColorGridMaxItemWidth + themeColorGridItemSpacing

    val columns = ((availableWidth + themeColorGridItemSpacing) / columnWidth).toInt()

    return columns.coerceAtLeast(themeColorGridMinColumns)
}

/**
 * Returns the width that each grid item should occupy.
 * The returned width never exceeds [themeColorGridMaxItemWidth].
 *
 * @param availableWidth Width available to the grid.
 * @param columns Number of columns in the grid.
 * @return Width to assign to each grid item.
 */
private fun themeColorGridItemWidthFor(
    availableWidth: Dp,
    columns: Int
): Dp {
    val totalSpacing = themeColorGridItemSpacing * (columns - 1)

    val availableItemWidth = (availableWidth - totalSpacing) / columns

    return availableItemWidth.coerceAtMost(themeColorGridMaxItemWidth)
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
                onClickLabel = stringResource(themeColor.displayNameRes),
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
            text = stringResource(themeColor.displayNameRes),
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
            text = stringResource(R.string.settings_brightness),
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
                onClickLabel = stringResource(themeMode.displayNameRes),
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
            text = stringResource(themeMode.displayNameRes),
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