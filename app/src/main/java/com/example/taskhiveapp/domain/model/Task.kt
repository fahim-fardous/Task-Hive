package com.example.taskhiveapp.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.taskhiveapp.presentation.task.model.TaskUiModel
import java.util.Date

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val plannedStartTime: Date? = null,
    val plannedEndTime: Date? = null,
    val plannedStartDate: Date? = null,
    val actualStartTime: Date? = null,
    val actualEndTime: Date? = null,
    var totalTimeSpend: Long = 0L,
    val project: Project,
    val taskStatus: TaskStatus = TaskStatus.TODO,
)

fun Task.toUiModel() =
    TaskUiModel(
        id = id,
        title = title,
        description = description,
        plannedStartTime = plannedStartTime ?: Date(),
        plannedEndTime = plannedEndTime ?: Date(),
        plannedStartDate = plannedStartDate ?: Date(),
        actualStartTime = actualStartTime ?: Date(),
        actualEndTime = actualEndTime ?: Date(),
        project = project,
        taskStatus = taskStatus,
        totalTimeSpend = totalTimeSpend,
    )
