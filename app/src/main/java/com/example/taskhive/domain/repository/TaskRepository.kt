package com.example.taskhive.domain.repository

import com.example.taskhive.domain.model.Log
import com.example.taskhive.domain.model.Project
import com.example.taskhive.domain.model.Task
import com.example.taskhive.domain.model.TaskStatus
import java.util.Date

interface TaskRepository {
    suspend fun saveTask(task: Task): Long


    suspend fun getTaskByProject(date: Date,project: Project): List<Task>

    suspend fun getTaskCountByProject(project: Project): Int

    suspend fun getTaskById(id: Int): Task

    suspend fun saveLog(log: Log): Long

    suspend fun getLogsByTaskId(taskId: Int): List<Log>

    suspend fun getInProgressTaskCount(): Int

    suspend fun getInProgressTaskCountByProject(project: Project): Int

    suspend fun deleteTask(taskId: Int)

    suspend fun getRecentInProgressTasks(): List<Task>

    suspend fun getCompletedTaskCount(project: Project): Int

    suspend fun getTodaysTasks(date:Date, project: Project?):List<Task>

    suspend fun getAllTasks(date: Date):List<Task>

}
