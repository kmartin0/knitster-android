package nl.kmartin.knitster.data.model

import java.time.Instant


data class Project(
    val id: Long = 0,
    val name: String = "",
    val icon: ProjectIcon = ProjectIcon.DEFAULT,
    val notes: String = "",
    val rowCounters: List<RowCounter> = emptyList(),
    val lastSavedAt: Instant,
    val createdAt: Instant
)