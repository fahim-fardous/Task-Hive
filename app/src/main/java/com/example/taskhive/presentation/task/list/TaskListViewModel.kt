package com.example.taskhive.presentation.task.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskhive.domain.model.Log
import com.example.taskhive.domain.model.Project
import com.example.taskhive.domain.model.TaskStatus
import com.example.taskhive.domain.model.toUiModel
import com.example.taskhive.domain.repository.ProjectRepository
import com.example.taskhive.domain.repository.TaskRepository
import com.example.taskhive.presentation.task.model.TaskUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val projectRepository: ProjectRepository
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<TaskUiModel>>(emptyList())
    val tasks = _tasks.asStateFlow()

    private val _project = MutableStateFlow<Project?>(null)
    val project = _project.asStateFlow()

    fun getTasksById(projectId: Int) = viewModelScope.launch {
        updateTasks(projectId)
    }

    fun getAllTasks() = viewModelScope.launch {
        updateTasks(null)
    }

    fun deleteTask(taskId: Int, projectId: Int? = null) = viewModelScope.launch {
        taskRepository.deleteTask(taskId)
        updateTasks(projectId)
    }

    private suspend fun updateTasks(projectId: Int?) {
        if (projectId != null) {
            val project = projectRepository.getProjectById(projectId)
            _project.value = project
            val tasks = taskRepository.getTaskByProject(project)
            _tasks.value = tasks.map { it.toUiModel() }
        } else {
            val tasks = taskRepository.getAllTasks()
            _tasks.value = tasks.map { it.toUiModel() }
        }
    }

    fun changeTaskStatus(taskId: Int, projectId: Int? = null, status: TaskStatus) = viewModelScope.launch {
        val task = taskRepository.getTaskById(taskId)
        val updatedTask = task.copy(taskStatus = status)
        taskRepository.saveTask(updatedTask)
        updateTasks(projectId)
    }

    fun getProjectById(projectId: Int) = viewModelScope.launch {
        val response = projectRepository.getProjectById(projectId)
        _project.value = response
    }

    fun saveLog(log: Log, projectId: Int? = null) = viewModelScope.launch {
        val id = taskRepository.saveLog(log)
        val task = taskRepository.getTaskById(log.taskId)
        val updatedTask = task.copy(
            actualStartTime = task.actualStartTime ?: log.startTime,
            totalTimeSpend = task.totalTimeSpend + log.duration,
            taskStatus = if ((task.totalTimeSpend + log.duration) > 0L) TaskStatus.IN_PROGRESS else TaskStatus.TODO,
        )
        taskRepository.saveTask(updatedTask)
        updateTasks(projectId)
    }
}