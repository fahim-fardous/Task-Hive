package com.example.taskhiveapp.domain.repository

import com.example.taskhiveapp.domain.model.Project

interface ProjectRepository {
    suspend fun saveProject(project: Project): Long

    suspend fun getAllProjects(): List<Project>

    suspend fun getProjectCount(): Int

    suspend fun getProjectById(projectId: Int): Project

    suspend fun getTaskCountByProject(project: Project): Int

    suspend fun getInProgressProjects(): List<Project>
}
