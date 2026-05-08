package com.example.secondbrain

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ThoughtDaoTest {
    private lateinit var db: AppDatabase
    private lateinit var dao: ThoughtDao

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        dao = db.thoughtDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun activeQueryHidesExpiredThoughts() = runBlocking {
        val now = 1_000L
        dao.insert(thought("expired", expiresAt = now))
        dao.insert(thought("active", expiresAt = now + 1))
        dao.insert(thought("forever", expiresAt = Long.MAX_VALUE))

        val contents = dao.getActiveThoughts(now).first().map { it.content }

        assertEquals(listOf("forever", "active"), contents)
    }

    @Test
    fun deleteExpiredRemovesExpiredThoughtsOnly() = runBlocking {
        val now = 1_000L
        dao.insert(thought("expired", expiresAt = now))
        dao.insert(thought("active", expiresAt = now + 1))

        dao.deleteExpired(now)

        val contents = dao.getActiveThoughts(0L).first().map { it.content }
        assertEquals(listOf("active"), contents)
    }

    private fun thought(content: String, expiresAt: Long) = ThoughtEntity(
        content = content,
        summary = "",
        createdAt = expiresAt,
        expiresAt = expiresAt
    )
}
