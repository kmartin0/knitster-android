package nl.kmartin.knitster.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import nl.kmartin.knitster.data.database.entity.ProjectEntity
import nl.kmartin.knitster.data.database.relation.ProjectEntityRelation
import nl.kmartin.knitster.data.model.ProjectIcon
import java.time.Instant

@Dao
interface ProjectDao {
    @Transaction
    @Query("SELECT * FROM projects WHERE id = :id")
    fun observeProject(id: Long): Flow<ProjectEntityRelation?>

    @Transaction
    @Query("SELECT * FROM projects ORDER BY lastSavedAt DESC")
    fun observeProjects(): Flow<List<ProjectEntityRelation>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(project: ProjectEntity): Long

    @Query("UPDATE projects SET name = :name, lastSavedAt = :lastSavedAt WHERE id = :id")
    suspend fun updateName(id: Long, name: String, lastSavedAt: Instant): Int

    @Query("UPDATE projects SET icon = :icon, lastSavedAt = :lastSavedAt WHERE id = :id")
    suspend fun updateIcon(id: Long, icon: ProjectIcon, lastSavedAt: Instant): Int

    @Query("UPDATE projects SET notes = :notes, lastSavedAt = :lastSavedAt WHERE id = :id")
    suspend fun updateNotes(id: Long, notes: String, lastSavedAt: Instant): Int

    @Query("UPDATE projects SET lastSavedAt = :lastSavedAt WHERE id = :id")
    suspend fun updateLastSaved(id: Long, lastSavedAt: Instant): Int

    @Delete
    suspend fun delete(project: ProjectEntity): Int
}