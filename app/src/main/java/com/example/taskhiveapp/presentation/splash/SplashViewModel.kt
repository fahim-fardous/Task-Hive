package com.example.taskhiveapp.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskhiveapp.Screen
import com.example.taskhiveapp.utils.PreferenceHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel
    @Inject
    constructor(
        private val preferenceHelper: PreferenceHelper,
    ) : ViewModel() {
        private val _isLoading = MutableStateFlow(true)
        val isLoading = _isLoading.asStateFlow()

        private val _startDestination = MutableStateFlow(Screen.OnBoard.route)
        val startDestination = _startDestination.asStateFlow()

        init {
            viewModelScope.launch {
                if (preferenceHelper.isOnboardingCompleted()) {
                    _startDestination.value = Screen.HomeIndex.route
                } else {
                    _startDestination.value = Screen.OnBoard.route
                }
                _isLoading.value = false
            }
        }
    }
