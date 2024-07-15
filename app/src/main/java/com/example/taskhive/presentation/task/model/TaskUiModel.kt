package com.example.taskhive.presentation.task.model

import com.example.taskhive.domain.model.Project
import com.example.taskhive.domain.model.TaskStatus
import java.util.Date

data class TaskUiModel(
    val id: Int,
    val title: String,
    val description: String,
    val plannedStartTime: Date,
    val plannedEndTime: Date,
    val actualStartTime: Date? = null,
    val actualEndTime: Date? = null,
    val project: Project,
    val taskStatus: TaskStatus = TaskStatus.TODO
)
