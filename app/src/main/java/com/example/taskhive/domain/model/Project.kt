package com.example.taskhive.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "projects")
data class Project(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    val endDate:Date,
    val selectedIcon: Int,
    val selectedIconColor: Int,
    val selectedBorderColor: Int,
)
