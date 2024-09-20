package com.example.taskhiveapp.presentation.log.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskhiveapp.domain.model.Log
import com.example.taskhiveapp.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogListViewModel
    @Inject
    constructor(
        private val taskRepository: TaskRepository,
    ) : ViewModel() {
        private val _logs = MutableStateFlow<List<Log>>(emptyList())
        val logs = _logs.asStateFlow()

        fun saveLog(log: Log) =
            viewModelScope.launch {
                taskRepository.saveLog(log)
                val logs = taskRepository.getLogsByTaskId(log.taskId)
                _logs.value = logs
            }

        fun getLogs(taskId: Int) =
            viewModelScope.launch {
                val logs = taskRepository.getLogsByTaskId(taskId)
                if (logs.isNotEmpty()) {
                    _logs.value = logs
                }
            }
    }
