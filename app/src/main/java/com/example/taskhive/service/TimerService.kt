package com.example.taskhive.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class TimerService:Service() {
    private lateinit var serviceJob:Job

    companion object{
        private val _timer = MutableStateFlow(0L)
        val timer = _timer.asStateFlow()
    }

    override fun onCreate() {
        Log.d("TimerService", "onCreate called")
        super.onCreate()
        serviceJob = CoroutineScope(Dispatchers.Default).launch {
            while (isActive){
                delay(1000L)
                _timer.value += 1000L
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        Log.d("TimerService", "onDestroy called")
        _timer.value = 0L
        serviceJob.cancel()
        super.onDestroy()
    }
    override fun onBind(intent: Intent?): IBinder? = null


}