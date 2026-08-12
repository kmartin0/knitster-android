package nl.kmartin.knitster.data.repository

import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import nl.kmartin.knitster.data.database.AppDatabase
import nl.kmartin.knitster.data.database.dao.ProjectDao
import nl.kmartin.knitster.data.database.dao.RowCounterDao
import nl.kmartin.knitster.data.mapper.toEntity
import nl.kmartin.knitster.data.mapper.toModel
import nl.kmartin.knitster.data.model.Project
import nl.kmartin.knitster.data.model.ProjectIcon
import nl.kmartin.knitster.data.model.RowCounter
import java.time.Instant
import javax.inject.Inject

class ProjectRepository @Inject constructor(
    private val appDatabase: AppDatabase,
    private val projectDao: ProjectDao,
    private val rowCounterDao: RowCounterDao
) {
    fun observeProjects(): Flow<List<Project>> {
        return projectDao
            .observeProjects()
            .map { projectEntityRelations ->
                projectEntityRelations.map { it.toModel() }
            }
    }

    fun observeProject(projectId: Long): Flow<Project?> {
        return projectDao
            .observeProject(projectId)
            .map { it?.toModel() }
    }

    suspend fun insertEmptyProject(): Long {
        val now = Instant.now()
        val id = projectDao.insert(
            Project(
                name = "",
                lastSavedAt = now,
                createdAt = now
            ).toEntity()
        )

        check(id > 0) { "Failed to insert project." }

        return id
    }

    suspend fun updateProjectName(projectId: Long, name: String) {
        val updatedRows = projectDao.updateName(projectId, name, Instant.now())
        check(updatedRows > 0) { "Project $projectId name was not updated." }
    }

    suspend fun updateProjectIcon(projectId: Long, icon: ProjectIcon) {
        val updatedRows = projectDao.updateIcon(projectId, icon, Instant.now())
        check(updatedRows > 0) { "Project $projectId icon was not updated." }
    }

    suspend fun updateProjectNotes(projectId: Long, notes: String) {
        val updatedRows = projectDao.updateNotes(projectId, notes, Instant.now())
        check(updatedRows > 0) { "Project $projectId notes were not updated." }
    }

    suspend fun deleteProject(project: Project) {
        val deletedRows = projectDao.delete(project.toEntity())
        check(deletedRows > 0) { "Project ${project.id} was not deleted." }
    }

    suspend fun addRowCounter(
        projectId: Long,
        rowCounter: RowCounter
    ) {
        appDatabase.withTransaction {
            rowCounterDao.incrementPositions(projectId)

            val id = rowCounterDao.insert(
                rowCounter.copy(id = 0L, position = 0).toEntity(projectId)
            )
            check(id > 0) { "Row counter was not added to project $projectId." }

            projectDao.updateLastSaved(id = projectId, lastSavedAt = Instant.now())
        }
    }

    suspend fun updateRowCounter(rowCounter: RowCounter, projectId: Long) {
        appDatabase.withTransaction {
            val updatedRows = rowCounterDao.update(rowCounter.toEntity(projectId))
            check(updatedRows > 0) { "Row counter ${rowCounter.id} was not updated." }

            projectDao.updateLastSaved(projectId, Instant.now())
        }
    }

    suspend fun updateRowCounterOrder(
        projectId: Long,
        rowCounterIds: List<Long>
    ) {
        appDatabase.withTransaction {
            rowCounterIds.forEachIndexed { position, rowCounterId ->
                val updatedRows = rowCounterDao.updatePosition(rowCounterId, position)

                check(updatedRows > 0) {
                    "Row counter $rowCounterId position was not updated."
                }
            }

            projectDao.updateLastSaved(projectId, Instant.now())
        }
    }

    suspend fun updateRowCounterCount(id: Long, projectId: Long, count: Int) {
        appDatabase.withTransaction {
            val updatedRows = rowCounterDao.updateCount(id, count)
            check(updatedRows > 0) { "Row counter $id count was not updated." }

            projectDao.updateLastSaved(projectId, Instant.now())
        }
    }

    suspend fun deleteRowCounter(rowCounter: RowCounter, projectId: Long) {
        appDatabase.withTransaction {
            val deletedRows = rowCounterDao.delete(rowCounter.toEntity(projectId))
            check(deletedRows > 0) { "Row counter ${rowCounter.id} was not deleted." }

            projectDao.updateLastSaved(projectId, Instant.now())
        }
    }
}