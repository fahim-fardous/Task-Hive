package com.example.taskhive.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "entries")
data class Entry(
    @PrimaryKey(autoGenerate = true)
    val entryId: Int = 0,
    val taskId: Int,
    val date: Date,
    val duration: Long = 0L,
)
