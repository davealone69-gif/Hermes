package com.example.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ChatMessage::class, Task::class, AgentConfig::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao
    abstract fun taskDao(): TaskDao
    abstract fun configDao(): ConfigDao
}
