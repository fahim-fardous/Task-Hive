package com.example.taskhive.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskhive.domain.model.Project
import com.example.taskhive.domain.model.TaskStatus
import com.example.taskhive.domain.model.toUiModel
import com.example.taskhive.domain.repository.ProjectRepository
import com.example.taskhive.domain.repository.TaskRepository
import com.example.taskhive.presentation.task.model.ProjectUiModel
import com.example.taskhive.presentation.task.model.TaskUiModel
import com.example.taskhive.utils.localDateToDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val taskRepository: TaskRepository,
        private val projectRepository: ProjectRepository,
    ) : ViewModel() {
        private val _projects = MutableStateFlow<List<ProjectUiModel>>(emptyList())
        val projects: StateFlow<List<ProjectUiModel>> =
            _projects

        private val _inProgressTasks = MutableStateFlow<List<TaskUiModel>>(emptyList())
        val inProgressTasks: StateFlow<List<TaskUiModel>> = _inProgressTasks

        private val _count = MutableStateFlow(0)
        val count: StateFlow<Int> = _count

        private val _progress = MutableStateFlow(0.0f)
        val progress: StateFlow<Float> = _progress

        fun getProjects() =
            viewModelScope.launch {
                val projects = projectRepository.getAllProjects()
                if (projects.isNotEmpty()) {
                    _projects.value =
                        projects.map {
                            val numberOfTask = getNumberOfTask(it)
                            val numberOfTaskCompleted = getNumberOfCompletedTask(it)
                            it.toUiModel().copy(
                                numberOfTask = numberOfTask,
                                progress = (numberOfTaskCompleted.toFloat() / numberOfTask.toFloat()),
                            )
                        }
                } else {
                }
            }

        fun getNumberOfProject() =
            viewModelScope.launch {
                val response = projectRepository.getProjectCount()
                _count.value = response
            }

        fun getInProgressTasks() =
            viewModelScope.launch {
                val tasks = taskRepository.getRecentInProgressTasks()
                if (tasks.isNotEmpty()) {
                    _inProgressTasks.value = tasks.map { it.toUiModel() }
                } else {
                    Log.d("Home", "getInProgressTasks: Nothing")
                }
            }

        fun getTaskProgress() =
            viewModelScope.launch {
                val tasks = taskRepository.getTodaysTasks(localDateToDate(LocalDate.now()), null)
                _progress.value =
                    (tasks.filter { it.taskStatus == TaskStatus.DONE }.size.toFloat() / tasks.size)
            }

        private suspend fun getNumberOfCompletedTask(project: Project): Int = taskRepository.getCompletedTaskCount(project)

        private suspend fun getNumberOfTask(project: Project): Int = projectRepository.getTaskCountByProject(project)

        private suspend fun getNumberOfInProgressTask(project: Project): Int = taskRepository.getInProgressTaskCountByProject(project)
    }
