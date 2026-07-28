package nl.kmartin.knitster.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp

/**
 * Applies the app's default border styling to this modifier.
 *
 * @param shape Shape of the border. Defaults to the medium shape from the current
 * [MaterialTheme].
 */
@Composable
fun Modifier.knitsterBorder(
    width: Dp = KnitsterBorder.Width,
    color: Color = KnitsterBorder.BorderColor,
    shape: Shape = KnitsterBorder.Shape
): Modifier {
    return this
        .clip(shape)
        .border(
            border = BorderStroke(width, color),
            shape = shape,
        )
}