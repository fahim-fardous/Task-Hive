package com.example.taskhive.presentation.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskhive.data.local.AppDatabase
import com.example.taskhive.domain.model.toProjectUiModel
import com.example.taskhive.domain.repository.ProjectRepository
import com.example.taskhive.domain.repository.TaskRepository
import com.example.taskhive.presentation.home.model.ProjectUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository,
) : ViewModel() {
    private val _projects = MutableStateFlow<List<ProjectUiModel>>(emptyList())
    val projects: StateFlow<List<ProjectUiModel>> = _projects

    private val _count = MutableStateFlow(0)
    val count: StateFlow<Int> = _count

    fun getProjects() =
        viewModelScope.launch {
            val response = projectRepository.getAllProjects(Date())
            if (response.isNotEmpty()) {
                _projects.value =
                    response.map {
                        it.toProjectUiModel().copy(numberOfTask = getTaskCount(it.id))
                    }
            }
        }

    fun getNumberOfProject(context: Context) =
        viewModelScope.launch {
            val response = AppDatabase(context).projectDao().getProjectCount()
            _count.value = response
        }

    private suspend fun getTaskCount(projectId: Int): Int {
        val project = projectRepository.getProjectById(projectId)
        return taskRepository.getTaskCountByProject(project)
    }
}
