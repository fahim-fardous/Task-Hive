package com.example.taskhive.domain.model

import androidx.room.Entity
import java.util.Date

@Entity(primaryKeys = ["id", "date"])
data class TaskEntryCrossRef(
    val id: Int,
    val date: Date
)
