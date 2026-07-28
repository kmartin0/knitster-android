package nl.kmartin.knitster.feature.projectlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.kmartin.knitster.R
import nl.kmartin.knitster.data.model.Project
import nl.kmartin.knitster.data.model.ProjectIcon
import nl.kmartin.knitster.theme.KnitsterBorder
import nl.kmartin.knitster.theme.knitsterBorder
import nl.kmartin.knitster.util.toFormattedLastSavedString
import java.time.Instant

@Composable
fun ProjectCard(
    project: Project,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .knitsterBorder(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ProjectCardIcon(iconRes = project.icon.drawableRes)
            ProjectCardInfo(
                modifier = Modifier.weight(1f),
                name = project.name,
                lastSavedAt = project.lastSavedAt,
            )
            ProjectCardChevron()
        }
    }
}

@Composable
private fun ProjectCardIcon(iconRes: Int) {
    Box(
        modifier = Modifier
            .fillMaxHeight(0.8f)
            .aspectRatio(1f)
            .clip(KnitsterBorder.Shape)
            .background(MaterialTheme.colorScheme.surfaceContainer),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            modifier = Modifier
                .fillMaxHeight(0.9f)
                .aspectRatio(1f),
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun ProjectCardInfo(
    modifier: Modifier = Modifier,
    name: String,
    lastSavedAt: Instant,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = "Last saved: ${lastSavedAt.toFormattedLastSavedString()}",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun ProjectCardChevron(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxHeight(),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            modifier = Modifier
                .fillMaxHeight(0.4f)
                .aspectRatio(1f),
            painter = painterResource(R.drawable.ic_chevron_forward),
            contentDescription = null
        )
    }
}

// --- Preview ---
@Preview(showBackground = true)
@Composable
private fun ProjectCardPreview() {
    ProjectCard(
        project = Project(
            id = 1,
            name = "Cosy Sweater",
            icon = ProjectIcon.DEFAULT,
            notes = "Very cosy",
            rowCount = 0,
            lastSavedAt = Instant.now(),
            createdAt = Instant.now()
        ),
        onClick = {}
    )
}