package com.example.taskhive.domain.model

import androidx.room.Embedded
import androidx.room.Relation

data class TaskWithEntries(
    @Embedded
    val tasks: Task,
    @Relation(
        parentColumn = "id",
        entityColumn = "taskId",
    )
    val entries: List<Entry>,
)
