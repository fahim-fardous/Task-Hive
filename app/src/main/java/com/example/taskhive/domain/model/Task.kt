package com.example.taskhive.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val description: String,
    //val plannedStartTime: Date,
    //val plannedEntTime: Date,
    //val actualStartTime: Date,
    //val actualEntTime: Date,
    val logo: Int,
    val status: String,
    val time:String,
    val projectId: Int,
    val taskStatus: TaskStatus = TaskStatus.TODO
)
