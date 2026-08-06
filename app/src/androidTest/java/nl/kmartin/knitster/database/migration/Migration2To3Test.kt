package nl.kmartin.knitster.database.migration

import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import nl.kmartin.knitster.data.database.AppDatabase
import nl.kmartin.knitster.data.database.migration.MIGRATION_2_3
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class Migration2To3Test {

    // Creates databases from the exported schema files (2.json, 3.json)
    // and manages the test database lifecycle.
    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java
    )

    @Test
    fun migrate2To3_movesRowCountIntoRowCounters() {
        // Create a database using the version 2 schema, then insert a project
        // using the old layout where rowCount lived on the projects table.
        helper.createDatabase(TEST_DB, 2).apply {
            execSQL(
                """
                INSERT INTO projects (id, name, icon, notes, rowCount, lastSavedAt, createdAt)
                VALUES (1, 'Sweater', 'knitting', '', 42, 1690000000000, 1690000000000)
                """.trimIndent()
            )
            close()
        }

        // Apply the migration and verify the resulting schema matches 3.json.
        val db = helper.runMigrationsAndValidate(TEST_DB, 3, true, MIGRATION_2_3)

        // Verify the data migrated correctly: the old rowCount should now
        // exist as a single row counter with the default name and no target.
        val cursor = db.query(
            "SELECT projectId, name, count, target FROM row_counters WHERE projectId = 1"
        )
        assertTrue(cursor.moveToFirst())
        assertEquals(1L, cursor.getLong(0))
        assertEquals("Row Counter", cursor.getString(1))
        assertEquals(42, cursor.getInt(2))
        assertTrue(cursor.isNull(3))

        // Confirm the old rowCount column was actually dropped from projects
        val columns = db.query("PRAGMA table_info(projects)").use { cursor ->
            buildList {
                while (cursor.moveToNext()) {
                    add(cursor.getString(1))
                }
            }
        }
        assertFalse(columns.contains("rowCount"))
    }

    companion object {
        // Temporary database used only for this migration test.
        private const val TEST_DB = "migration-test"
    }
}