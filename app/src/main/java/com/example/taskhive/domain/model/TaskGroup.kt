package com.example.taskhive.domain.model

data class TaskGroup(
    val id:Int,
    val name:String,
    val projects:List<Project>
)
