package nl.kmartin.knitster.data.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import nl.kmartin.knitster.data.database.dao.ProjectDao
import nl.kmartin.knitster.data.mapper.toEntity
import nl.kmartin.knitster.data.mapper.toModel
import nl.kmartin.knitster.data.model.Project
import java.time.Instant
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

class ProjectRepository @Inject constructor(
    private val projectDao: ProjectDao
) {
    fun observeProjects(): Flow<List<Project>> {
        return projectDao
            .observeProjects()
            .onEach {
                delay(5.seconds)
            }
            .map { projectEntities ->
                projectEntities.map { it.toModel() }
            }
    }

    fun observeProject(projectId: Long): Flow<Project?> {
        return projectDao
            .observeProject(projectId)
            .onEach {
                delay(5.seconds)
            }
            .map { it?.toModel() }
    }

    suspend fun insertEmptyProject(): Long {
        val now = Instant.now()
        return projectDao.insert(
            Project(
                lastSavedAt = now,
                createdAt = now
            ).toEntity()
        )
    }

    suspend fun updateProject(project: Project): Int {
        return projectDao.update(project.copy(lastSavedAt = Instant.now()).toEntity())
    }

    suspend fun deleteProject(project: Project): Int {
        return projectDao.delete(project.toEntity())
    }
}