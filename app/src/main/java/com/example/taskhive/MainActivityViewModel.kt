package com.example.taskhive

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskhive.domain.model.Entry
import com.example.taskhive.domain.model.TaskStatus
import com.example.taskhive.domain.repository.TaskRepository
import com.example.taskhive.utils.localDateToDate
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
                    if (task.taskStatus != TaskStatus.DONE &&
                        task.plannedStartDate?.before(
                            localDateToDate(LocalDate.now()),
                        ) == true
                    ) {
                        val entryCount = taskRepository.getEntryByDate(localDateToDate(LocalDate.now()), taskId = task.id)
                        if(entryCount==0){
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
