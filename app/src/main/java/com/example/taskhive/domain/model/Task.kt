package com.example.taskhive.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.taskhive.presentation.task.model.TaskUiModel
import java.util.Date

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val description: String,
    val plannedStartTime: Date,
    val plannedEndTime: Date,
    val actualStartTime: Date? = null,
    val actualEndTime: Date? = null,
    val project: Project,
    val taskStatus: TaskStatus = TaskStatus.TODO,
)

fun Task.toTaskUiModel() =
    TaskUiModel(
        id = id,
        title = title,
        description = description,
        plannedStartTime = plannedStartTime,
        plannedEndTime = plannedEndTime,
        actualStartTime = actualStartTime,
        actualEndTime = actualEndTime,
        project = project,
        taskStatus = taskStatus,
    )
