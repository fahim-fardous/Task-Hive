package com.example.taskhive.domain.repository

import com.example.taskhive.domain.model.Project
import com.example.taskhive.domain.model.TaskStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date

interface ProjectRepository {
    suspend fun saveProject(project: Project): Long

    suspend fun getAllProjects(): List<Project>

    suspend fun getProjectCount(): Int

    suspend fun getProjectById(projectId: Int): Project

    suspend fun getTaskCountByProject(project: Project): Int
}
