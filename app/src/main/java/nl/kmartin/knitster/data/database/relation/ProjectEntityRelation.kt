package nl.kmartin.knitster.data.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import nl.kmartin.knitster.data.database.entity.ProjectEntity
import nl.kmartin.knitster.data.database.entity.RowCounterEntity

data class ProjectEntityRelation(
    @Embedded val project: ProjectEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "projectId"
    )
    val rowCounters: List<RowCounterEntity>
)