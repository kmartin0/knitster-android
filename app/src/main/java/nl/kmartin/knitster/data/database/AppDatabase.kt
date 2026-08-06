package nl.kmartin.knitster.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import nl.kmartin.knitster.data.database.converter.InstantConverter
import nl.kmartin.knitster.data.database.converter.ProjectIconConverter
import nl.kmartin.knitster.data.database.dao.ProjectDao
import nl.kmartin.knitster.data.database.dao.RowCounterDao
import nl.kmartin.knitster.data.database.entity.ProjectEntity
import nl.kmartin.knitster.data.database.entity.RowCounterEntity

@Database(
    entities = [ProjectEntity::class, RowCounterEntity::class],
    version = 3
)
@TypeConverters(InstantConverter::class, ProjectIconConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun rowCounterDao(): RowCounterDao
}