package com.example.secondbrain

import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppDatabaseMigrationTest {
    private val dbName = "migration-test"
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun deleteExistingDb() {
        context.deleteDatabase(dbName)
    }

    @After
    fun cleanUpDb() {
        context.deleteDatabase(dbName)
    }

    @Test
    fun migrationFromOneToTwoPreservesLegacyThoughts() {
        createVersionOneDb()

        val db = Room.databaseBuilder(context, AppDatabase::class.java, dbName)
            .addMigrations(MIGRATION_1_2)
            .build()

        val cursor = db.openHelper.readableDatabase.query(
            "SELECT content, expiresAt FROM thoughts WHERE id = 1"
        )

        cursor.use {
            it.moveToFirst()
            assertEquals("legacy", it.getString(0))
            assertEquals(Long.MAX_VALUE, it.getLong(1))
        }

        db.close()
    }

    private fun createVersionOneDb() {
        val file = context.getDatabasePath(dbName)
        SQLiteDatabase.openOrCreateDatabase(file, null).use { db ->
            db.execSQL(
                """
                CREATE TABLE thoughts (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    content TEXT NOT NULL,
                    summary TEXT NOT NULL,
                    createdAt INTEGER NOT NULL
                )
                """.trimIndent()
            )
            db.execSQL(
                """
                INSERT INTO thoughts (id, content, summary, createdAt)
                VALUES (1, 'legacy', '', 1000)
                """.trimIndent()
            )
            db.version = 1
        }
    }
}
