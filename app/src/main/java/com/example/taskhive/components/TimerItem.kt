package com.example.taskhive.components

import kotlinx.coroutines.flow.MutableStateFlow

data class TimerItem(
    val id:Int,
    var time:MutableStateFlow<Long> = MutableStateFlow(0L)
)
