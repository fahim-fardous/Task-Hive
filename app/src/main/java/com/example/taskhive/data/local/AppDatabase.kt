package com.example.taskhive.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.taskhive.data.local.dao.ProjectDao
import com.example.taskhive.data.local.dao.TaskDao
import com.example.taskhive.domain.model.Project
import com.example.taskhive.domain.model.Task

@Database(entities = [Task::class, Project::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao

    abstract fun taskDao(): TaskDao

    companion object {
        operator fun invoke(context: Context) = buildDatabase(context)

        private fun buildDatabase(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "TaskHive.db").build()
    }
}
