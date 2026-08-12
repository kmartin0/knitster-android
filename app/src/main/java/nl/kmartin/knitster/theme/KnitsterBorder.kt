package nl.kmartin.knitster.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

object KnitsterBorder {
    val Width = KnitsterDimensions.DefaultBorderWidth

    val Color: Color
        @Composable get() = MaterialTheme.colorScheme.outline

    val Shape: Shape
        @Composable get() = MaterialTheme.shapes.medium
}