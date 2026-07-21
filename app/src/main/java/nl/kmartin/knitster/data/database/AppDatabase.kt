package nl.kmartin.knitster.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import nl.kmartin.knitster.data.database.converter.InstantConverter
import nl.kmartin.knitster.data.database.dao.ProjectDao
import nl.kmartin.knitster.data.database.entity.ProjectEntity

@Database(
    entities = [ProjectEntity::class],
    version = 1
)
@TypeConverters(InstantConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
}