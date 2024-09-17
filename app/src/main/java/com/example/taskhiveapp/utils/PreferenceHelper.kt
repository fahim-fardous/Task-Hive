package com.example.taskhiveapp.utils

import android.content.SharedPreferences

class PreferenceHelper(
    private val preferences: SharedPreferences,
) {
    companion object {
        private const val PREF_ONBOARDING_COMPLETED = "pref_onboarding_completed"
    }

    fun setOnboardingCompleted() {
        preferences.edit().putBoolean(PREF_ONBOARDING_COMPLETED, true).apply()
    }

    fun isOnboardingCompleted(): Boolean = preferences.getBoolean(PREF_ONBOARDING_COMPLETED, false)
}
