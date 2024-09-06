package com.example.taskhive.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "days")
data class Day(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: Date,
    val totalTimeSpend: Long,
)
