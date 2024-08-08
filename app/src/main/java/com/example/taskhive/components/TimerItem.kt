package com.example.taskhive.components

import kotlinx.coroutines.flow.MutableStateFlow

data class TimerItem(
    var time:MutableStateFlow<Long> = MutableStateFlow(0L),
    var isRunning:Boolean = false,
)
