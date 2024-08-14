package com.example.taskhive.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class TimerBroadcastReceiver(private val onTimerStopped: () -> Unit) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Call the provided callback when the timer is stopped
        if (intent.action == "TIMER_STOPPED_ACTION") {
            onTimerStopped()
        }
    }
}
