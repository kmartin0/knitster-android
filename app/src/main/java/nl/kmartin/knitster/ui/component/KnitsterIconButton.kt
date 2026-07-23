package nl.kmartin.knitster.ui.component



import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import nl.kmartin.knitster.theme.KnitsterBorder
import nl.kmartin.knitster.theme.knitsterBorder

/**
 * Displays a square icon button using the app's default styling.
 *
 * @param modifier Modifier to be applied to the root layout.
 * @param onClick Called when the button is clicked.
 * @param iconRes Resource ID of the icon to display.
 * @param contentDescription Description of the button for accessibility.
 * @param shape Shape of the button and its border.
 * @param tint Tint applied to the icon.
 * @param iconSize Modifier applied to the icon.
 */
@Composable
fun KnitsterIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    iconRes: Int,
    contentDescription: String,
    shape: Shape = MaterialTheme.shapes.medium,
    tint: Color = Color.Black,
    iconSize: Modifier = Modifier.fillMaxSize(0.9f)
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(shape)
            .knitsterBorder(shape = shape)
            .clickable(
                onClick = onClick,
                role = Role.Button,
                onClickLabel = contentDescription,
                indication = ripple(bounded = true),
                interactionSource = remember { MutableInteractionSource() },
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null, // Description is provided by the clickable modifier.
            modifier = iconSize
                .padding(KnitsterBorder.Width),
            tint = tint,
        )
    }
}