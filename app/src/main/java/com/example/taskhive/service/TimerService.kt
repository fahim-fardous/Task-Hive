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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TimerService : Service() {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    companion object {
        private val _timerItem = MutableStateFlow<TimerItem?>(null)
        val timerItem = _timerItem.asStateFlow()
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
        val currentTimer = _timerItem.value

        if (currentTimer?.isRunning == true) {
            println("Another task is already running. Ignoring the new request.")
        } else {
            startTimer(taskId)
        }
        return START_STICKY
    }

    private fun startTimer(taskId: Int) {
        stopTimer()

        _timerItem.value =
            TimerItem(
                taskId = taskId,
                isRunning = true,
            )

        coroutineScope.launch {
            if (_timerItem.value == null) {
                return@launch
            }
            while (_timerItem.value!!.isRunning) {
                delay(1000L)
                _timerItem.value =
                    _timerItem.value?.copy(time = _timerItem.value!!.time.plus(1000L))
            }
        }
    }

    private fun stopTimer() {
        _timerItem.value = null
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        stopTimer()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
