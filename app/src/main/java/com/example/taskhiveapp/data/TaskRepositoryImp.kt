package com.example.taskhiveapp.data

import com.example.taskhiveapp.data.local.AppDatabase
import com.example.taskhiveapp.domain.model.Entry
import com.example.taskhiveapp.domain.model.Log
import com.example.taskhiveapp.domain.model.Project
import com.example.taskhiveapp.domain.model.ProjectProgress
import com.example.taskhiveapp.domain.model.Task
import com.example.taskhiveapp.domain.model.TaskWithEntries
import com.example.taskhiveapp.domain.repository.TaskRepository
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

        override suspend fun saveEntry(entry: Entry): Long =
            withContext(Dispatchers.IO) {
                db.taskDao().saveEntry(entry)
            }

        override suspend fun getTaskByProject(
            date: Date,
            project: Project,
        ): List<Task> =
            withContext(Dispatchers.IO) {
                db.taskDao().getTaskByProject(date, project)
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

        override suspend fun getCompletedTaskCountByProject(project: Project): Int =
            withContext(Dispatchers.IO) {
                db.taskDao().getNumberOfCompletedTaskByProject(project)
            }

        override suspend fun getCompletedTaskCount(startDate: Date): Int =
            withContext(Dispatchers.IO) {
                db.taskDao().getNumberOfCompletedTask(startDate)
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

        override suspend fun getProgressForWeek(
            startDate: Date,
            endDate: Date,
        ): List<ProjectProgress> =
            withContext(Dispatchers.IO) {
                db.taskDao().getProgressForWeek(startDate, endDate)
            }

        override suspend fun getWeeklyTask(
            startDate: Date,
            endDate: Date,
        ): List<Task> =
            withContext(Dispatchers.IO) {
                db.taskDao().getWeeklyTask(startDate, endDate)
            }

        override suspend fun getTaskByRange(
            startDate: Date,
            endDate: Date,
        ): List<Task> =
            withContext(Dispatchers.IO) {
                db.taskDao().getTaskByRange(startDate, endDate)
            }

        override suspend fun getTaskByRangeByProject(
            startDate: Date,
            endDate: Date,
            project: Project,
        ): List<Task> =
            withContext(Dispatchers.IO) {
                db.taskDao().getTaskByRangeAndProject(startDate, endDate, project)
            }

        override suspend fun getAllTask(): List<Task> =
            withContext(Dispatchers.IO) {
                db.taskDao().getAllTask()
            }

        override suspend fun getTaskWithEntries(date: Date): List<TaskWithEntries> =
            withContext(Dispatchers.IO) {
                db.taskDao().getTaskWithEntries(date)
            }

        override suspend fun getAllEntry(): List<Entry> =
            withContext(Dispatchers.IO) {
                db.taskDao().getAllEntry()
            }

        override suspend fun getEntryByDate(
            date: Date,
            taskId: Int,
        ): Int =
            withContext(Dispatchers.IO) {
                db.taskDao().getEntryByDate(date, taskId)
            }
    }