package com.example.taskhive.presentation.task.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskhive.domain.model.Day
import com.example.taskhive.domain.model.Log
import com.example.taskhive.domain.model.Project
import com.example.taskhive.domain.model.TaskStatus
import com.example.taskhive.domain.model.toUiModel
import com.example.taskhive.domain.repository.DayRepository
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
class TaskListViewModel
    @Inject
    constructor(
        private val taskRepository: TaskRepository,
        private val projectRepository: ProjectRepository,
        private val dayRepository: DayRepository,
    ) : ViewModel() {
        private val _tasks = MutableStateFlow<List<TaskUiModel>>(emptyList())
        val tasks = _tasks.asStateFlow()

        private val _project = MutableStateFlow<Project?>(null)
        val project = _project.asStateFlow()

        fun getProjectById(projectId: Int) =
            viewModelScope.launch {
                _project.value = projectRepository.getProjectById(projectId)
            }

        fun getTasks(
            date: LocalDate,
            projectId: Int? = null,
        ) = viewModelScope.launch {
            if (projectId == null) {
                _tasks.value = taskRepository.getAllTasks(localDateToDate(date)).map { it.toUiModel() }
            } else {
                println("Hi I am coming here to get tasks")
                val project = projectRepository.getProjectById(projectId)
                _tasks.value =
                    taskRepository
                        .getTaskByProject(localDateToDate(date), project)
                        .map { it.toUiModel() }
            }
        }

        private suspend fun getTaskByProject(
            projectId: Int? = null,
            date: LocalDate,
        ) {
            if (projectId == null) {
                _tasks.value = taskRepository.getAllTasks(localDateToDate(date)).map { it.toUiModel() }
            } else {
                val project = projectRepository.getProjectById(projectId)
                _tasks.value =
                    taskRepository
                        .getTaskByProject(localDateToDate(date), project)
                        .map { it.toUiModel() }
            }
        }

        fun deleteTask(
            taskId: Int,
            projectId: Int? = null,
            date: LocalDate,
        ) = viewModelScope.launch {
            taskRepository.deleteTask(taskId)
            getTaskByProject(projectId, date)
        }

        fun changeTaskStatus(
            taskId: Int,
            projectId: Int? = null,
            date: LocalDate,
            status: TaskStatus,
        ) = viewModelScope.launch {
            val task = taskRepository.getTaskById(taskId)
            taskRepository.saveTask(task.copy(taskStatus = status))
            getTaskByProject(projectId, date)
        }

        fun saveLog(
            log: Log,
            projectId: Int? = null,
            date: LocalDate,
        ) = viewModelScope.launch {
            taskRepository.saveLog(log)
            val task = taskRepository.getTaskById(log.taskId)
            val updatedTask =
                task.copy(
                    actualStartTime = task.actualStartTime ?: log.startTime,
                    totalTimeSpend = task.totalTimeSpend + log.duration,
                    taskStatus = if ((task.totalTimeSpend + log.duration) > 0L) TaskStatus.IN_PROGRESS else TaskStatus.TODO,
                )
            taskRepository.saveTask(updatedTask)
            getTaskByProject(projectId, date)
        }

        fun addTime(time: Long, date:LocalDate) =
            viewModelScope.launch {
                val day = dayRepository.getDay(localDateToDate(date))
                if(day == null){
                    dayRepository.saveDay(Day(date = localDateToDate(date), totalTimeSpend = time))
                }else{
                    dayRepository.saveDay(day.copy(totalTimeSpend = day.totalTimeSpend + time))
                }

            }

        private fun localDateToDate(date: LocalDate): Date = Date.from(date.atStartOfDay(ZoneOffset.UTC).toInstant())
    }
