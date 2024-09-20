package com.example.taskhiveapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskhiveapp.domain.model.Entry
import com.example.taskhiveapp.domain.model.TaskStatus
import com.example.taskhiveapp.domain.repository.TaskRepository
import com.example.taskhiveapp.utils.localDateToDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel
    @Inject
    constructor(
        private val taskRepository: TaskRepository,
    ) : ViewModel() {
        fun incompleteTask() =
            viewModelScope.launch {
                val entries = taskRepository.getAllEntry()
                entries.forEach { entry ->
                    val task = taskRepository.getTaskById(entry.taskId)
                    println("${entry.taskId} is $task")
                    if (task != null &&
                        task.taskStatus != TaskStatus.DONE &&
                        task.plannedStartDate?.before(
                            localDateToDate(LocalDate.now()),
                        ) == true
                    ) {
                        val entryCount =
                            taskRepository.getEntryByDate(
                                localDateToDate(LocalDate.now()),
                                taskId = task.id,
                            )
                        if (entryCount == 0) {
                            taskRepository.saveEntry(
                                Entry(
                                    date = localDateToDate(LocalDate.now()),
                                    taskId = task.id,
                                    duration = 0L,
                                ),
                            )
                        }
                    }
                }
            }
    }
