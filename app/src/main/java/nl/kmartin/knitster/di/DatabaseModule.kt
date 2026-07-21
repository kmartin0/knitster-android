package nl.kmartin.knitster.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import nl.kmartin.knitster.data.database.AppDatabase
import nl.kmartin.knitster.data.database.dao.ProjectDao
import javax.inject.Singleton

/**
 * Hilt module that provides the application's Room database and its DAOs.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val DATABASE_NAME = "knitster.db"

    /**
     * Creates the singleton Room database instance.
     *
     * @param context The context used to create the database.
     * @return The application's [AppDatabase] instance.
     */
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = AppDatabase::class.java,
            name = DATABASE_NAME
        )
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }

    /**
     * Provides the [ProjectDao] for accessing note data.
     *
     * @param database The application's Room database.
     * @return The [ProjectDao] instance.
     */
    @Provides
    fun provideProjectDao(database: AppDatabase): ProjectDao {
        return database.projectDao()
    }
}