package com.example.taskhive.domain.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class EntryWithTasks(
    @Embedded val entry: Entry,
    @Relation(
        parentColumn = "date",
        entityColumn = "id",
        associateBy = Junction(TaskEntryCrossRef::class)
    )
    val tasks: List<Task>
)
