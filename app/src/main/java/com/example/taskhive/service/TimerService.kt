package com.example.taskhive.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.taskhive.components.TimerItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TimerService : Service() {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private val id = MutableStateFlow(0L)

    companion object {
        private val _taskMap = MutableStateFlow<Map<Int, TimerItem>>(emptyMap())
        val taskMap: StateFlow<Map<Int, TimerItem>> = _taskMap.asStateFlow()
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int,
    ): Int {
        intent?.getIntExtra("taskId", 0)?.let { startTimer(it) }
        return START_STICKY
    }

    private fun startTimer(taskId: Int) {
        stopTimer(taskId)
        val timerItem = _taskMap.value[taskId] ?: TimerItem().apply { _taskMap.value += (taskId to this) }
        if (!timerItem.isRunning) {
            timerItem.isRunning = true
            coroutineScope.launch {
                while (timerItem.isRunning) {
                    delay(1000L)
                    timerItem.time.value += 1000L
                    _taskMap.value = _taskMap.value.toMutableMap().apply { put(taskId, timerItem) }
                }
            }
            stopTimer(taskId)
        }

    }

    private fun stopTimer(taskId: Int) {
        println("Completed")
        _taskMap.value = _taskMap.value.toMutableMap().apply {
            this[taskId]?.time?.value = 0L
            remove(taskId)
        }
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
