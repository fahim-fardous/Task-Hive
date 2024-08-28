package com.example.taskhive.domain.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "entries",
)
data class Entry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val startDate: Date,
    val taskId: Int,
)
