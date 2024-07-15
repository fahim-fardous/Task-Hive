package com.example.taskhive.domain.repository

import com.example.taskhive.domain.model.Project
import com.example.taskhive.domain.model.Task

interface TaskRepository {
    suspend fun saveTask(task: Task): Long

    suspend fun getAllTasks(): List<Task>

    suspend fun getTaskByProject(project: Project): List<Task>

    suspend fun getTaskCountByProject(project: Project): Int

    suspend fun getTaskById(id: Int): Task
}
