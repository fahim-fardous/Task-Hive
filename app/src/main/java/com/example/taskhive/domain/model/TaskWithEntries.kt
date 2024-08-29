package com.example.taskhive.domain.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class TaskWithEntries(
    @Embedded val task: Task,
    @Relation(
        parentColumn = "id",
        entityColumn = "date",
        associateBy = Junction(TaskEntryCrossRef::class)
    )
    val entries: List<Entry>
)