package com.example.taskhive.domain.model

import androidx.room.Entity

@Entity(primaryKeys = ["id", "taskId"])
data class TaskEntryCrossRef(
    val id: Int,
    val taskId: Int,
)
