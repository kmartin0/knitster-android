package nl.kmartin.knitster.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.visible
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.kmartin.knitster.R
import nl.kmartin.knitster.theme.KnitsterBorder
import nl.kmartin.knitster.theme.ThemeMode
import nl.kmartin.knitster.theme.knitsterBorder


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
fun ThemeModePicker(
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
private fun ThemeModePickerPreview() {
    ThemeModePicker(
        currentThemeMode = ThemeMode.DEFAULT,
        onThemeModeSelected = {}
    )
}
