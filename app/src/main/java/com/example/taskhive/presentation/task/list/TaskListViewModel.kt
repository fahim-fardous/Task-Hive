package com.example.taskhive.presentation.task.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskhive.domain.model.Project
import com.example.taskhive.domain.model.toTaskUiModel
import com.example.taskhive.domain.repository.ProjectRepository
import com.example.taskhive.domain.repository.TaskRepository
import com.example.taskhive.presentation.task.model.TaskUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel
    @Inject
    constructor(
        private val projectRepository: ProjectRepository,
        private val taskRepository: TaskRepository,
    ) : ViewModel() {
        private val _tasks = MutableStateFlow<List<TaskUiModel>>(emptyList())
        val tasks = _tasks.asStateFlow()

        private val _project = MutableStateFlow<Project?>(null)
        val project = _project.asStateFlow()

        fun getTasksById(projectId: Int) =
            viewModelScope.launch {
                val project = projectRepository.getProjectById(projectId)
                val response = taskRepository.getTaskByProject(project)
                if (response.isNotEmpty()) {
                    _tasks.value = response.map { it.toTaskUiModel() }
                }
            }

        fun getAllTasks() =
            viewModelScope.launch {
                val response = taskRepository.getAllTasks()
                if (response.isNotEmpty()) {
                    _tasks.value = response.map { it.toTaskUiModel() }
                }
            }

        fun getProjectById(projectId: Int) =
            viewModelScope.launch {
                val response = projectRepository.getProjectById(projectId)
                _project.value = response
            }
    }
