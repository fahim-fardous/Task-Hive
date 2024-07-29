package com.example.taskhive.presentation.log.list

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskhive.data.local.AppDatabase
import com.example.taskhive.domain.model.Log
import com.example.taskhive.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogListViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {
    private val _logs = MutableStateFlow<List<Log>>(emptyList())
    val logs = _logs.asStateFlow()

    fun getLogs(taskId: Int) = viewModelScope.launch {
        val logs = taskRepository.getLogsByTaskId(taskId)
        if (logs.isNotEmpty()) {
            _logs.value = logs
        }
    }
}