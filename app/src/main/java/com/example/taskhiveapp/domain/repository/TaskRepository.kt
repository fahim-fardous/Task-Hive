package com.example.taskhiveapp.domain.repository

import com.example.taskhiveapp.domain.model.Entry
import com.example.taskhiveapp.domain.model.Log
import com.example.taskhiveapp.domain.model.Project
import com.example.taskhiveapp.domain.model.ProjectProgress
import com.example.taskhiveapp.domain.model.Task
import com.example.taskhiveapp.domain.model.TaskWithEntries
import java.util.Date

interface TaskRepository {
    suspend fun saveTask(task: Task): Long

    suspend fun saveEntry(entry: Entry): Long

    suspend fun getTaskByProject(
        date: Date,
        project: Project,
    ): List<Task>

    suspend fun getTaskCountByProject(project: Project): Int

    suspend fun getTaskById(id: Int): Task

    suspend fun saveLog(log: Log): Long

    suspend fun getLogsByTaskId(taskId: Int): List<Log>

    suspend fun getInProgressTaskCount(): Int

    suspend fun getInProgressTaskCountByProject(project: Project): Int

    suspend fun deleteTask(taskId: Int)

    suspend fun getRecentInProgressTasks(): List<Task>

    suspend fun getCompletedTaskCountByProject(project: Project): Int

    suspend fun getCompletedTaskCount(startDate: Date): Int

    suspend fun getTodaysTasks(
        date: Date,
        project: Project?,
    ): List<Task>

    suspend fun getAllTasks(date: Date): List<Task>

    suspend fun getProgressForWeek(
        startDate: Date,
        endDate: Date,
    ): List<ProjectProgress>

    suspend fun getWeeklyTask(
        startDate: Date,
        endDate: Date,
    ): List<Task>

    suspend fun getTaskByRange(
        startDate: Date,
        endDate: Date,
    ): List<Task>

    suspend fun getTaskByRangeByProject(
        startDate: Date,
        endDate: Date,
        project: Project,
    ): List<Task>

    suspend fun getAllTask(): List<Task>

    suspend fun getTaskWithEntries(date: Date): List<TaskWithEntries>

    suspend fun getAllEntry(): List<Entry>

    suspend fun getEntryByDate(
        date: Date,
        taskId: Int,
    ): Int
}
