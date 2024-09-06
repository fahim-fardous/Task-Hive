package com.example.taskhive.presentation.task.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskhive.domain.model.Task
import com.example.taskhive.domain.model.TaskStatus
import com.example.taskhive.domain.model.toUiModel
import com.example.taskhive.domain.repository.ProjectRepository
import com.example.taskhive.domain.repository.TaskRepository
import com.example.taskhive.presentation.task.model.TaskUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class TaskEditViewModel
    @Inject
    constructor(
        private val taskRepository: TaskRepository,
        private val projectRepository: ProjectRepository,
    ) : ViewModel() {
        private val _task = MutableStateFlow<TaskUiModel?>(null)
        val task = _task.asStateFlow()

        fun getTaskById(taskId: Int) =
            viewModelScope.launch {
                val task = taskRepository.getTaskById(taskId)
                _task.value = task.toUiModel()
            }

        fun editTask(task: TaskUiModel) =
            viewModelScope.launch {
                taskRepository.saveTask(
                    Task(
                        id = task.id,
                        title = task.title,
                        description = task.description,
                        plannedStartTime = task.plannedStartTime,
                        plannedEndTime = task.plannedEndTime,
                        plannedStartDate = task.plannedStartDate,
                        project = task.project,
                        taskStatus = task.taskStatus,
                        totalTimeSpend = task.totalTimeSpend,
                    ),
                )
            }

        fun onTitleChange(newTitle: String) {
            _task.value = task.value?.copy(title = newTitle)
        }

        fun onDescriptionChange(newDescription: String) {
            _task.value = task.value?.copy(description = newDescription)
        }

        fun onTaskStatusChange(newStatus: TaskStatus) {
            _task.value = task.value?.copy(taskStatus = newStatus)
        }

        private fun localDateToDate(date: LocalDate): Date = Date.from(date.atStartOfDay(ZoneOffset.UTC).toInstant())
    }
