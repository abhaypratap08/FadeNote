package com.example.secondbrain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "thoughts")
data class ThoughtEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val content: String,
    val summary: String,
    val createdAt: Long = System.currentTimeMillis(),
    val expiresAt: Long
)
