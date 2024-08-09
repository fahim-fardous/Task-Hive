package com.example.taskhive.domain.model

import androidx.room.Entity
import java.util.Date

@Entity(tableName = "days")
data class Day(
    val id: Int,
    val date: Date,
    val totalTimeSpend: Long,
)
