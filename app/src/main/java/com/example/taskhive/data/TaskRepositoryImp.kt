package com.example.taskhive.data

import com.example.taskhive.data.local.AppDatabase
import com.example.taskhive.domain.model.Log
import com.example.taskhive.domain.model.Project
import com.example.taskhive.domain.model.Task
import com.example.taskhive.domain.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

        override suspend fun getAllTasks(): List<Task> =
            withContext(Dispatchers.IO) {
                db.taskDao().getAllTasks()
            }

        override suspend fun getTaskByProject(project: Project): List<Task> =
            withContext(Dispatchers.IO) {
                db.taskDao().getTaskByProject(project)
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

//        override suspend fun getCompletedTaskCount(taskStatus: TaskStatus): Int =
//            withContext(Dispatchers.IO) {
//                db.taskDao().getNumberOfCompletedTask(taskStatus)
//            }
    }
