package com.example.taskhive.presentation.task.list

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskhive.data.local.AppDatabase
import com.example.taskhive.domain.model.Project
import com.example.taskhive.domain.model.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskListViewModel : ViewModel() {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks = _tasks.asStateFlow()

    private val _project = MutableStateFlow<Project?>(null)
    val project = _project.asStateFlow()

    fun getTasksById(
        projectId: Int,
        context: Context,
    ) = viewModelScope.launch {
        val response = AppDatabase(context).taskDao().getTaskByProjectId(projectId)
        if (response.isNotEmpty()) {
            _tasks.value = response
        }
    }

    fun getAllTasks(context: Context) =viewModelScope.launch {
        val response = AppDatabase(context).taskDao().getAllTasks()
        if (response.isNotEmpty()) {
            _tasks.value = response
        }
    }

    fun getProjectById(projectId: Int, context: Context) = viewModelScope.launch {
        val response = AppDatabase(context).projectDao().getProjectById(projectId)
            _project.value = response
    }
}
