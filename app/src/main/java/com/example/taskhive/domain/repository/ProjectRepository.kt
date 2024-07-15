package com.example.taskhive.domain.repository

import com.example.taskhive.domain.model.Project
import java.util.Date

interface ProjectRepository {
    suspend fun saveProject(project: Project): Long

    suspend fun getAllProjects(currentDate: Date): List<Project>

    suspend fun getProjectCount(): Int

    suspend fun getProjectById(projectId: Int): Project
}
