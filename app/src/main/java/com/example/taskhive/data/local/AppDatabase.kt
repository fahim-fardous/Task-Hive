package com.example.taskhive.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.taskhive.components.DataTypeConverters
import com.example.taskhive.data.local.dao.DayDao
import com.example.taskhive.data.local.dao.ProjectDao
import com.example.taskhive.data.local.dao.TaskDao
import com.example.taskhive.domain.model.Day
import com.example.taskhive.domain.model.Entry
import com.example.taskhive.domain.model.EntryWithTasks
import com.example.taskhive.domain.model.Log
import com.example.taskhive.domain.model.Project
import com.example.taskhive.domain.model.Task
import com.example.taskhive.domain.model.TaskEntryCrossRef
import com.example.taskhive.domain.model.TaskWithEntries

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
        operator fun invoke(context: Context) = buildDatabase(context)

        private fun buildDatabase(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "TaskHive.db").build()
    }
}
