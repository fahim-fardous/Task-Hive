package com.example.taskhive.domain.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class EntryWithTasks(
    @Embedded val entry: Entry,
    @Relation(
        parentColumn = "taskId",
        entityColumn = "id",
    )
    val tasks: List<Task>
)
