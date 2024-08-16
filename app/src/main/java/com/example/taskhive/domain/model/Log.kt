package com.example.taskhive.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "logs")
data class Log(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val duration: Long,
    val startDate: Date,
    val endDate: Date,
    val startTime: Date,
    val endTime: Date,
    val taskId: Int,
)
