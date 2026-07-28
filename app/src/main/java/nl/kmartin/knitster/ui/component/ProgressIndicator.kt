package nl.kmartin.knitster.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Displays a circular progress indicator sized and styled for use in an app bar.
 */
@Composable
fun AppBarCircularProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color = LocalContentColor.current
) {
    CircularProgressIndicator(
        modifier = modifier
            .padding(horizontal = 12.dp)
            .size(24.dp),
        strokeWidth = 2.dp,
        color = color
    )
}