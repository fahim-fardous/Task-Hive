package com.example.taskhiveapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.taskhiveapp.components.DataTypeConverters
import com.example.taskhiveapp.data.local.dao.DayDao
import com.example.taskhiveapp.data.local.dao.ProjectDao
import com.example.taskhiveapp.data.local.dao.TaskDao
import com.example.taskhiveapp.domain.model.Day
import com.example.taskhiveapp.domain.model.Entry
import com.example.taskhiveapp.domain.model.Log
import com.example.taskhiveapp.domain.model.Project
import com.example.taskhiveapp.domain.model.Task
import com.example.taskhiveapp.domain.model.TaskEntryCrossRef

@Database(
    entities = [Task::class, Project::class, Log::class, Day::class, Entry::class, TaskEntryCrossRef::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(DataTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao

    abstract fun taskDao(): TaskDao

    abstract fun dayDao(): DayDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(context, AppDatabase::class.java, "TaskHive.db")
                    .build()
                    .also { instance = it }
            }
        }

        fun clearInstance() {
            instance = null
        }
    }

}
