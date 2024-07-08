package com.example.taskhive.domain.model

data class Project(
    val id:Int,
    val name:String,
    val todos:List<Todo>
)
