package nl.kmartin.knitster.data.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Migrates the database from version 2 to 3 by moving the project's single
 * `rowCount` into a new `row_counters` table, allowing each project to have
 * multiple row counters.
 *
 * Each existing project's `rowCount` is migrated to a row counter named
 * "Row Counter".
 */
val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // 1. Create the new row_counters table, with a foreign key back to projects
        db.execSQL(
            """
            CREATE TABLE row_counters (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                projectId INTEGER NOT NULL,
                name TEXT NOT NULL,
                count INTEGER NOT NULL,
                target INTEGER,
                position INTEGER NOT NULL,
                FOREIGN KEY(projectId) REFERENCES projects(id) ON DELETE CASCADE
            )
            """.trimIndent()
        )

        // Index name must match Room's generated convention (index_<table>_<column>) or schema validation will fail.
        db.execSQL("CREATE INDEX index_row_counters_projectId ON row_counters(projectId)")

        // 2. turn each project's existing rowCount into a single row counter with name "Row Counter" and no target.
        db.execSQL(
            """
            INSERT INTO row_counters (projectId, name, count, target, position)
            SELECT id, 'Row Counter', rowCount, NULL, 0 FROM projects
            """.trimIndent()
        )

        // 3. SQLite doesn't support dropping columns directly, so recreate the
        //    projects table without rowCount and copy the existing data across.
        db.execSQL(
            """
            CREATE TABLE projects_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                name TEXT NOT NULL,
                icon TEXT NOT NULL,
                notes TEXT NOT NULL,
                lastSavedAt INTEGER NOT NULL,
                createdAt INTEGER NOT NULL
            )
            """.trimIndent()
        )
        db.execSQL(
            """
            INSERT INTO projects_new (id, name, icon, notes, lastSavedAt, createdAt)
            SELECT id, name, icon, notes, lastSavedAt, createdAt FROM projects
            """.trimIndent()
        )
        db.execSQL("DROP TABLE projects")
        db.execSQL("ALTER TABLE projects_new RENAME TO projects")
    }
}