package nl.kmartin.knitster.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import nl.kmartin.knitster.data.model.ProjectIcon
import java.time.Instant

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String = "",
    val icon: ProjectIcon = ProjectIcon.DEFAULT,
    val notes: String = "",
    val rowCount: Int = 0,
    val lastSavedAt: Instant,
    val createdAt: Instant
)
