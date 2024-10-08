package com.example.taskhiveapp.presentation.task.model

import com.example.taskhiveapp.domain.model.Project
import com.example.taskhiveapp.domain.model.TaskStatus
import java.util.Date

data class TaskUiModel(
    val id: Int = 0,
    var title: String,
    var description: String,
    var plannedStartTime: Date? = null,
    var plannedEndTime: Date? = null,
    var plannedStartDate: Date? = null,
    val actualStartTime: Date? = null,
    val actualEndTime: Date? = null,
    val project: Project,
    var taskStatus: TaskStatus = TaskStatus.TODO,
    var totalTimeSpend: Long = 0L,
)
