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
import com.example.taskhive.utils.getReadableDate
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

        fun getProjectById(
            projectId: Int,
        ) = viewModelScope.launch {
            _project.value = projectRepository.getProjectById(projectId)
        }

        fun getTasks(
            fromDate: LocalDate,
            toDate: LocalDate? = null,
            projectId: Int? = null,
        ) = viewModelScope.launch {
            getTaskByProject(fromDate, toDate, projectId)
            println("Calling project block from get tasks")
        }

        fun getTaskByRange(
            fromDate: LocalDate,
            toDate: LocalDate? = null,
            projectId: Int? = null,
        ) = viewModelScope.launch {
            getTaskByProject(fromDate, toDate, projectId)
            println("Calling project block from get task by range")
        }

        private suspend fun getTaskByProject(
            fromDate: LocalDate,
            toDate: LocalDate? = null,
            projectId: Int? = null,
        ) {
            println("To project block")
            if (projectId == null) {
                if (toDate != null) {
                    _tasks.value =
                        taskRepository
                            .getTaskByRange(
                                localDateToDate(fromDate),
                                localDateToDate(toDate),
                            ).map { it.toUiModel() }
                } else {
                    _tasks.value =
                        taskRepository.getAllTasks(localDateToDate(fromDate)).map { it.toUiModel() }
                }
            } else {
                val project = projectRepository.getProjectById(projectId)
                if (toDate != null) {
                    _tasks.value =
                        taskRepository
                            .getTaskByRangeByProject(
                                localDateToDate(fromDate),
                                localDateToDate(toDate),
                                project,
                            ).map { it.toUiModel() }
                } else {
                    _tasks.value =
                        taskRepository
                            .getTaskByProject(localDateToDate(fromDate), project)
                            .map { it.toUiModel() }
                }
            }
        }

        fun deleteTask(
            taskId: Int,
            fromDate: LocalDate,
            toDate: LocalDate? = null,
            projectId: Int? = null,
        ) = viewModelScope.launch {
            taskRepository.deleteTask(taskId)
            getTaskByProject(fromDate, toDate, projectId)
            println("Calling project block from delete task")
        }

        fun changeTaskStatus(
            taskId: Int,
            fromDate: LocalDate,
            toDate: LocalDate? = null,
            projectId: Int? = null,
            status: TaskStatus,
        ) = viewModelScope.launch {
            val task = taskRepository.getTaskById(taskId)
            taskRepository.saveTask(task.copy(taskStatus = status))
            getTaskByProject(fromDate, toDate, projectId)
            println("Calling project block from change task status")
        }

        fun saveLog(
            log: Log,
            fromDate: LocalDate,
            toDate: LocalDate? = null,
            projectId: Int? = null,
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
            getTaskByProject(fromDate, toDate, projectId)
            println("Calling project block from save log")
        }

        fun addTime(
            time: Long,
            fromDate: LocalDate,
            toDate: LocalDate? = null,
        ) = viewModelScope.launch {
            val day = dayRepository.getDay(localDateToDate(fromDate))
            if (day == null) {
                dayRepository.saveDay(Day(date = localDateToDate(fromDate), totalTimeSpend = time))
            } else {
                dayRepository.saveDay(day.copy(totalTimeSpend = day.totalTimeSpend + time))
            }
        }

        private fun localDateToDate(date: LocalDate): Date = Date.from(date.atStartOfDay(ZoneOffset.UTC).toInstant())
    }
