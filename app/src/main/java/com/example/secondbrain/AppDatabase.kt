package com.example.secondbrain

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ThoughtEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun thoughtDao(): ThoughtDao
}
