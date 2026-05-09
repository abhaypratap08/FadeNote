package com.example.secondbrain

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ThoughtDao {

    @Insert
    suspend fun insert(thought: ThoughtEntity)

    @Query("SELECT * FROM thoughts WHERE expiresAt > :now ORDER BY createdAt DESC")
    fun getActiveThoughts(now: Long): Flow<List<ThoughtEntity>>

    @Query("DELETE FROM thoughts WHERE expiresAt <= :now")
    suspend fun deleteExpired(now: Long)

    @Query("DELETE FROM thoughts")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(thought: ThoughtEntity)
}
