package com.example.taskhive.presentation.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskhive.data.local.AppDatabase
import com.example.taskhive.domain.model.Project
import com.example.taskhive.domain.model.toUiModel
import com.example.taskhive.domain.repository.ProjectRepository
import com.example.taskhive.domain.repository.TaskRepository
import com.example.taskhive.presentation.task.model.ProjectUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val projectRepository: ProjectRepository,
) : ViewModel() {
    private val _projects = MutableStateFlow<List<ProjectUiModel>>(emptyList())
    val projects: StateFlow<List<ProjectUiModel>> =
        _projects

    private val _count = MutableStateFlow(0)
    val count: StateFlow<Int> = _count

    fun getProjects() = viewModelScope.launch {
        val response = projectRepository.getAllProjects()
        if (response.isNotEmpty()) {
            _projects.value = response.map {
                it.toUiModel().copy(numberOfTask = getNumberOfTask(it))
            }
        }
        else{
            println("Nothing")
        }
    }

    fun getNumberOfProject() = viewModelScope.launch {
        val response = projectRepository.getProjectCount()
        _count.value = response
    }

    suspend fun getNumberOfTask(project: Project):Int  {
        return projectRepository.getTaskCountByProject(project)
    }
}