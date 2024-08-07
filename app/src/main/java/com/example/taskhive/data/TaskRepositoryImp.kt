package com.example.taskhive.data

import com.example.taskhive.data.local.AppDatabase
import com.example.taskhive.domain.model.Log
import com.example.taskhive.domain.model.Project
import com.example.taskhive.domain.model.Task
import com.example.taskhive.domain.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

class TaskRepositoryImp
    @Inject
    constructor(
        private val db: AppDatabase,
    ) : TaskRepository {
        override suspend fun saveTask(task: Task): Long =
            withContext(Dispatchers.IO) {
                db.taskDao().saveTask(task)
            }

        override suspend fun getTaskByProject(date: Date, project: Project): List<Task> =
            withContext(Dispatchers.IO) {
                db.taskDao().getTaskByProject(date,project)
            }

        override suspend fun getTaskCountByProject(project: Project): Int =
            withContext(Dispatchers.IO) {
                db.taskDao().getTaskCountByProject(project)
            }

        override suspend fun getTaskById(id: Int): Task =
            withContext(Dispatchers.IO) {
                db.taskDao().getTaskById(id)
            }

        override suspend fun saveLog(log: Log): Long =
            withContext(Dispatchers.IO) {
                db.taskDao().saveLog(log)
            }

        override suspend fun getLogsByTaskId(taskId: Int): List<Log> =
            withContext(Dispatchers.IO) {
                db.taskDao().getLogsByTaskId(taskId)
            }

        override suspend fun getInProgressTaskCount(): Int =
            withContext(Dispatchers.IO) {
                db.taskDao().getNumberOfInProgressTask()
            }

        override suspend fun getInProgressTaskCountByProject(project: Project): Int =
            withContext(Dispatchers.IO) {
                db.taskDao().getInProgressTaskCountByProject(project)
            }

        override suspend fun deleteTask(taskId: Int) =
            withContext(Dispatchers.IO) {
                db.taskDao().deleteTask(taskId)
            }

        override suspend fun getRecentInProgressTasks(): List<Task> =
            withContext(Dispatchers.IO) {
                db.taskDao().getInProgressTasks()
            }

        override suspend fun getCompletedTaskCount(project: Project): Int =
            withContext(Dispatchers.IO) {
                db.taskDao().getNumberOfCompletedTask(project)
            }

        override suspend fun getTodaysTasks(
            date: Date,
            project: Project?,
        ): List<Task> =
            withContext(Dispatchers.IO) {
                db.taskDao().getTodaysTasks(date, project)
            }

        override suspend fun getAllTasks(date: Date): List<Task> =
            withContext(Dispatchers.IO) {
                db.taskDao().getAllTasks(date)
            }
    }
