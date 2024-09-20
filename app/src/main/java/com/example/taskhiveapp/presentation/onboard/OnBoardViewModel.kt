package com.example.taskhiveapp.presentation.onboard

import androidx.lifecycle.ViewModel
import com.example.taskhiveapp.utils.PreferenceHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardViewModel
    @Inject
    constructor(
        private val preferenceHelper: PreferenceHelper,
    ) : ViewModel() {
        fun setOnboardingCompleted() {
            preferenceHelper.setOnboardingCompleted()
        }
    }
