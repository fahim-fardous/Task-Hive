package com.example.taskhive.presentation.task.add

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskhive.data.local.AppDatabase
import com.example.taskhive.domain.model.Task
import com.example.taskhive.utils.getReadableTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskAddViewModel : ViewModel() {
    private val _showMessage = MutableStateFlow<String?>(null)
    val showMessage = _showMessage.asStateFlow()

    private fun isValid(
        name: String,
        description: String,
        plannedStartTime: String,
        plannedEndTime: String,
    ): Boolean {
        if (name.isBlank() || description.isBlank() || plannedStartTime.isBlank() || plannedEndTime.isBlank()) {
            _showMessage.value = "Fill all the fields"
            return false
        }
        return true
    }

    fun saveTask(
        task: Task,
        context: Context,
    ) = viewModelScope.launch {
        if (!isValid(
                task.title,
                task.description,
                task.plannedStartTime.getReadableTime(),
                task.plannedEndTime.getReadableTime(),
            )
        ) {
            return@launch
        }
        AppDatabase(context).taskDao().saveTask(task)
        _showMessage.value = "Task saved"
    }
}
