package nl.kmartin.knitster.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import nl.kmartin.knitster.R
import nl.kmartin.knitster.theme.KnitsterBorder
import nl.kmartin.knitster.theme.knitsterBorder

/**
 * Displays a selectable settings item.
 *
 * The selected item is highlighted with a thicker border and a check icon.
 * An optional [startIcon] can be displayed before the label.
 *
 * @param label Text displayed for the item.
 * @param isSelected Whether this item is currently selected.
 * @param onClick Called when the item is selected.
 * @param modifier Modifier to be applied to the item.
 * @param startIcon Optional icon displayed before the label.
 */
@Composable
internal fun SettingsSelectableItem(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    startIcon: Painter? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .knitsterBorder(
                color = MaterialTheme.colorScheme.primary,
                width = if (isSelected) {
                    KnitsterBorder.Width * 2
                } else {
                    KnitsterBorder.Width
                }
            )
            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
            .padding(8.dp)
            .clickable(
                onClick = onClick,
                role = Role.RadioButton,
                onClickLabel = label,
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        startIcon?.let { painter ->
            Icon(
                painter = painter,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            modifier = Modifier.visible(isSelected),
            painter = painterResource(R.drawable.ic_check_circle),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
    }
}