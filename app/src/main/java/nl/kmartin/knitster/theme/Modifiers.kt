package nl.kmartin.knitster.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape

/**
 * Applies the app's default border styling to this modifier.
 *
 * @param shape Shape of the border. Defaults to the medium shape from the current
 * [MaterialTheme].
 */
@Composable
fun Modifier.knitsterBorder(shape: Shape = MaterialTheme.shapes.medium): Modifier {
    return this
        .border(
            border = BorderStroke(KnitsterBorder.Width, KnitsterBorder.BorderColor),
            shape = shape,
        )
}