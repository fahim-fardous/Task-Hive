package com.example.taskhive.presentation.task.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskhive.domain.model.toTaskUiModel
import com.example.taskhive.domain.repository.TaskRepository
import com.example.taskhive.presentation.task.model.TaskUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskEditViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
):ViewModel() {
    private val _task = MutableStateFlow<TaskUiModel?>(null)
    val task: StateFlow<TaskUiModel?> = _task

    fun getTaskById(taskId: Int) = viewModelScope.launch{
        val response = taskRepository.getTaskById(taskId)
        _task.value = response.toTaskUiModel(
        )
    }
}