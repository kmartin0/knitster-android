package nl.kmartin.knitster.data.mapper

import nl.kmartin.knitster.data.database.entity.ProjectEntity
import nl.kmartin.knitster.data.database.relation.ProjectEntityRelation
import nl.kmartin.knitster.data.model.Project

fun ProjectEntity.toModel(): Project =
    Project(
        id = id,
        name = name,
        icon = icon,
        notes = notes,
        lastSavedAt = lastSavedAt,
        createdAt = createdAt
    )

fun Project.toEntity(): ProjectEntity =
    ProjectEntity(
        id = id,
        name = name,
        icon = icon,
        notes = notes,
        lastSavedAt = lastSavedAt,
        createdAt = createdAt
    )

fun ProjectEntityRelation.toModel(): Project =
    Project(
        id = project.id,
        name = project.name,
        icon = project.icon,
        notes = project.notes,
        rowCounters = rowCounters.sortedByDescending { it.id }.map { it.toModel() },
        lastSavedAt = project.lastSavedAt,
        createdAt = project.createdAt
    )
