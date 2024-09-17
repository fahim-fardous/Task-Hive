package com.example.taskhiveapp.presentation.task.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskhiveapp.domain.model.Entry
import com.example.taskhiveapp.domain.model.Task
import com.example.taskhiveapp.domain.model.TaskStatus
import com.example.taskhiveapp.domain.repository.ProjectRepository
import com.example.taskhiveapp.domain.repository.TaskRepository
import com.example.taskhiveapp.utils.getReadableDate
import com.example.taskhiveapp.utils.getReadableTime
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
            if (name.isBlank() ||
                description.isBlank() ||
                plannedStartTime.isBlank() ||
                plannedEndTime.isBlank() ||
                plannedDate.isBlank()
            ) {
                _showMessage.value = "Fill all the fields"
                return false
            }
            return true
        }

        fun saveTask(
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
                    startDate.getReadableDate(),
                )
            ) {
                return@launch
            }
            if (plannedStartTime.after(plannedEndTime)) {
                _showMessage.value = "Start time cannot be after end time"
                return@launch
            }
            val project = projectRepository.getProjectById(projectId)
            val id =
                taskRepository.saveTask(
                    Task(
                        title = title,
                        description = description,
                        plannedStartTime = plannedStartTime,
                        plannedEndTime = plannedEndTime,
                        plannedStartDate = startDate,
                        project = project,
                        taskStatus = TaskStatus.TODO,
                    ),
                )
            taskRepository.saveEntry(
                Entry(
                    date = startDate ?: Date(),
                    taskId = id.toInt(),
                ),
            )
            _showMessage.value = "Task saved"
        }

        fun updateMessage() =
            viewModelScope.launch {
                _showMessage.value = null
            }
    }
