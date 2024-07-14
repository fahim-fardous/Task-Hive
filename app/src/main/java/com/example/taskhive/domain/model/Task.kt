package com.example.taskhive.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val description: String,
    val plannedStartTime: Date? = null,
    val plannedEndTime: Date? = null,
    val actualStartTime: Date? = null,
    val actualEndTime: Date? = null,
    val projectId: Int,
    val taskStatus: TaskStatus = TaskStatus.TODO
)
