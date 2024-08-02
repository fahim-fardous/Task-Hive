package com.example.taskhive.domain.repository

import com.example.taskhive.domain.model.Log
import com.example.taskhive.domain.model.Project
import com.example.taskhive.domain.model.Task
import com.example.taskhive.domain.model.TaskStatus

interface TaskRepository {
    suspend fun saveTask(task: Task): Long

    suspend fun getAllTasks(): List<Task>

    suspend fun getTaskByProject(project: Project): List<Task>

    suspend fun getTaskCountByProject(project: Project): Int

    suspend fun getTaskById(id: Int): Task

    suspend fun saveLog(log: Log): Long

    suspend fun getLogsByTaskId(taskId: Int): List<Log>

    suspend fun getInProgressTaskCount(): Int

    suspend fun getInProgressTaskCountByProject(project: Project): Int

    suspend fun deleteTask(taskId: Int)

    suspend fun getRecentInProgressTasks(): List<Task>

    suspend fun getCompletedTaskCount(project: Project): Int

}
