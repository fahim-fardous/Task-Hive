package com.example.taskhive.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.taskhive.components.TimerItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
        val taskId = intent?.getIntExtra("taskId", 0) ?: 0
        val isAnyTaskRunning = _taskMap.value.values.any { it.isRunning.value }

        if (!isAnyTaskRunning) {
            startTimer(taskId)
        } else {
            // Another task is already running, so don't start a new timer
            println("Another task is already running. Ignoring the new request.")
        }
        return START_STICKY
    }

    private fun startTimer(taskId: Int) {
        stopTimer(taskId) // Stop any existing timer for this task if needed
        val timerItem =
            _taskMap.value[taskId] ?: TimerItem().apply { _taskMap.value += (taskId to this) }

        if (!timerItem.isRunning.value) {
            timerItem.isRunning.value = true
            coroutineScope.launch {
                while (timerItem.isRunning.value) {
                    delay(1000L)
                    timerItem.time.value += 1000L
                    _taskMap.value = _taskMap.value.toMutableMap().apply { put(taskId, timerItem) }
                }
            }
        }
    }

    private fun stopTimer(taskId: Int) {
        _taskMap.value =
            _taskMap.value.toMutableMap().apply {
                this[taskId]?.isRunning?.value = false
                this[taskId]?.time?.value = 0L
                remove(taskId)
            }
    }

    override fun onDestroy() {
        coroutineScope.cancel()

        // Reset all timers when the service is destroyed
        _taskMap.value =
            _taskMap.value.toMutableMap().apply {
                forEach { (taskId, timerItem) ->
                    timerItem.isRunning.value = false
                    timerItem.time.value = 0L
                }
                clear()
            }

        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
