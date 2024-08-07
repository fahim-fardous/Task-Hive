package com.example.taskhive.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.taskhive.components.TimerItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class TimerService : Service() {
    private  var serviceJob: Job? = null

    companion object {
        private val _timer = MutableStateFlow(0L)
        val timer = _timer.asStateFlow()
        private val _isTimerRunning = MutableStateFlow(false)
        val isTimerRunning = _isTimerRunning.asStateFlow()
        private val _id = MutableStateFlow(0)
        val id = _id.asStateFlow()

        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int,
    ): Int {
        when (intent?.action) {
            ACTION_START -> {
                startTimer(intent.getIntExtra("taskId", 0))
            }
        }
        return START_STICKY
    }

    override fun stopService(name: Intent?): Boolean {
        stopTimer()
        return super.stopService(name)
    }

    private fun startTimer(taskId: Int){
        _id.value = taskId
        stopTimer()
        _isTimerRunning.value = true
        serviceJob =
            CoroutineScope(Dispatchers.Default).launch {
                while (isActive) {
                    println("Coming and updating")
                    delay(1000L)
                    _timer.value += 1000L
                }
            }
        _timer.value = 0L
    }

    private fun stopTimer(){
        _isTimerRunning.value = false
        _timer.value = 0L
        serviceJob?.cancel()
    }

    override fun onDestroy() {
        stopTimer()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
