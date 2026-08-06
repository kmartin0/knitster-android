package nl.kmartin.knitster.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import nl.kmartin.knitster.data.database.entity.RowCounterEntity

@Dao
interface RowCounterDao {
    @Insert
    suspend fun insert(rowCounter: RowCounterEntity): Long

    @Update
    suspend fun update(rowCounter: RowCounterEntity): Int

    @Query("UPDATE row_counters SET count = :count WHERE id = :id")
    suspend fun updateCount(id: Long, count: Int): Int

    @Delete
    suspend fun delete(rowCounter: RowCounterEntity): Int
}