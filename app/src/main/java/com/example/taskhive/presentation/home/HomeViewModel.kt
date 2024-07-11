package com.example.taskhive.presentation.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskhive.data.local.AppDatabase
import com.example.taskhive.domain.model.Project
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel:ViewModel() {
    private val _projects = MutableStateFlow<List<Project>>(emptyList())
    val projects: StateFlow<List<Project>> = _projects

    private val _count = MutableStateFlow<Int>(0)
    val count: StateFlow<Int> = _count

    fun getProjects(context:Context) = viewModelScope.launch{
        val response = AppDatabase(context).projectDao().getAllProjects()
        if(response.isNotEmpty()){
            _projects.value = response
        }
    }

    fun getNumberOfProject(context: Context) = viewModelScope.launch {
        val response = AppDatabase(context).projectDao().getProjectCount()
        _count.value = response
    }
}