package nl.kmartin.knitster.data.mapper

import nl.kmartin.knitster.data.database.entity.RowCounterEntity
import nl.kmartin.knitster.data.model.RowCounter

fun RowCounterEntity.toModel(): RowCounter =
    RowCounter(
        id = id,
        name = name,
        count = count,
        target = target
    )

fun RowCounter.toEntity(projectId: Long): RowCounterEntity =
    RowCounterEntity(
        id = id,
        projectId = projectId,
        name = name,
        count = count,
        target = target
    )