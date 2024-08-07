package com.example.taskhive.presentation.task.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskhive.domain.model.Task
import com.example.taskhive.domain.model.TaskStatus
import com.example.taskhive.domain.repository.ProjectRepository
import com.example.taskhive.domain.repository.TaskRepository
import com.example.taskhive.utils.getReadableDate
import com.example.taskhive.utils.getReadableTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class TaskAddViewModel
    @Inject
    constructor(
        private val projectRepository: ProjectRepository,
        private val taskRepository: TaskRepository,
    ) : ViewModel() {
        private val _showMessage = MutableStateFlow<String?>(null)
        val showMessage = _showMessage.asStateFlow()

        private fun isValid(
            name: String,
            description: String,
            plannedStartTime: String,
            plannedEndTime: String,
            plannedDate: String,
        ): Boolean {
            if (name.isBlank() || description.isBlank() || plannedStartTime.isBlank() || plannedEndTime.isBlank()|| plannedDate.isBlank()) {
                _showMessage.value = "Fill all the fields"
                return false
            }
            return true
        }

        fun saveTask(
            id: Int,
            title: String,
            description: String,
            plannedStartTime: Date,
            plannedEndTime: Date,
            projectId: Int,
            startDate: Date?,
        ) = viewModelScope.launch {
            if (!isValid(
                    title,
                    description,
                    plannedStartTime.getReadableTime(),
                    plannedEndTime.getReadableTime(),
                startDate.getReadableDate()
                )
            ) {
                return@launch
            }
            val project = projectRepository.getProjectById(projectId)
            taskRepository.saveTask(
                Task(
                    id = id,
                    title = title,
                    description = description,
                    plannedStartTime = plannedStartTime,
                    plannedEndTime = plannedEndTime,
                    plannedStartDate = startDate,
                    project = project,
                    taskStatus = TaskStatus.TODO,
                ),
            )
            _showMessage.value = "Task saved"
        }
    }
