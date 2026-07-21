package nl.kmartin.knitster.data.mapper

import nl.kmartin.knitster.data.database.entity.ProjectEntity
import nl.kmartin.knitster.data.model.Project

fun ProjectEntity.toModel(): Project =
    Project(
        id = id,
        name = name,
        icon = icon,
        notes = notes,
        rowCount = rowCount,
        lastSavedAt = lastSavedAt,
        createdAt = createdAt
    )

fun Project.toEntity(): ProjectEntity =
    ProjectEntity(
        id = id,
        name = name,
        icon = icon,
        notes = notes,
        rowCount = rowCount,
        lastSavedAt = lastSavedAt,
        createdAt = createdAt
    )