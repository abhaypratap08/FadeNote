package com.example.secondbrain

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ThoughtDao {

    @Insert
    suspend fun insert(thought: ThoughtEntity)

    @Query("SELECT * FROM thoughts ORDER BY createdAt DESC")
    fun getAllThoughts(): Flow<List<ThoughtEntity>>

    @Query("DELETE FROM thoughts")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(thought: ThoughtEntity)
}
