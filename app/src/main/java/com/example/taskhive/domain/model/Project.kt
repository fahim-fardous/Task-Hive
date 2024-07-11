package com.example.taskhive.domain.model

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class Project(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    val selectedIcon:String,
    val selectedIconColor: Int,
    val selectedBorderColor:Int
)
