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
        return projectDao.insert(
            Project(
                name = "",
                lastSavedAt = now,
                createdAt = now
            ).toEntity()
        )
    }

    suspend fun updateProjectName(projectId: Long, name: String): Int {
        return projectDao.updateName(projectId, name, Instant.now())
    }

    suspend fun updateProjectIcon(projectId: Long, icon: ProjectIcon): Int {
        return projectDao.updateIcon(projectId, icon, Instant.now())
    }

    suspend fun updateProjectNotes(projectId: Long, notes: String): Int {
        return projectDao.updateNotes(projectId, notes, Instant.now())
    }

    suspend fun deleteProject(project: Project): Int {
        return projectDao.delete(project.toEntity())
    }

    suspend fun addRowCounter(
        projectId: Long,
        rowCounter: RowCounter
    ): Long {
        return appDatabase.withTransaction {
            val newId = rowCounterDao.insert(rowCounter.copy(id = 0L).toEntity(projectId))
            projectDao.updateLastSaved(id = projectId, lastSavedAt = Instant.now())
            newId
        }
    }

    suspend fun updateRowCounter(rowCounter: RowCounter, projectId: Long): Int {
        return appDatabase.withTransaction {
            val rowsAffected = rowCounterDao.update(rowCounter.toEntity(projectId))
            projectDao.updateLastSaved(projectId, Instant.now())
            rowsAffected
        }
    }

    suspend fun updateRowCounterCount(id: Long, projectId: Long, count: Int): Int {
        return appDatabase.withTransaction {
            val rowsAffected = rowCounterDao.updateCount(id, count)
            projectDao.updateLastSaved(projectId, Instant.now())
            rowsAffected
        }
    }

    suspend fun deleteRowCounter(rowCounter: RowCounter, projectId: Long): Int {
        return appDatabase.withTransaction {
            val rowsAffected = rowCounterDao.delete(rowCounter.toEntity(projectId))
            projectDao.updateLastSaved(projectId, Instant.now())
            rowsAffected
        }
    }
}