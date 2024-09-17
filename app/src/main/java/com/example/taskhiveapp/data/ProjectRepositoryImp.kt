package com.example.taskhiveapp.data

import com.example.taskhiveapp.data.local.AppDatabase
import com.example.taskhiveapp.domain.model.Project
import com.example.taskhiveapp.domain.repository.ProjectRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProjectRepositoryImp
    @Inject
    constructor(
        private val db: AppDatabase,
    ) : ProjectRepository {
        override suspend fun saveProject(project: Project): Long =
            withContext(Dispatchers.IO) {
                db.projectDao().saveProject(project)
            }

        override suspend fun getAllProjects(): List<Project> =
            withContext(Dispatchers.IO) {
                db.projectDao().getAllProjects()
            }

        override suspend fun getProjectCount(): Int =
            withContext(Dispatchers.IO) {
                db.projectDao().getProjectCount()
            }

        override suspend fun getProjectById(projectId: Int): Project =
            withContext(Dispatchers.IO) {
                db.projectDao().getProjectById(projectId)
            }

        override suspend fun getTaskCountByProject(project: Project): Int =
            withContext(Dispatchers.IO) {
                db.taskDao().getTaskCountByProject(project)
            }

        override suspend fun getInProgressProjects(): List<Project> =
            withContext(Dispatchers.IO) {
                db
                    .taskDao()
                    .getInProgressTasks()
                    .map { it.project }
                    .distinct()
            }
    }
