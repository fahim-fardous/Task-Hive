package com.example.taskhive.presentation.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskhive.domain.model.Day
import com.example.taskhive.domain.model.Task
import com.example.taskhive.domain.repository.DayRepository
import com.example.taskhive.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.temporal.TemporalAdjusters
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel
    @Inject
    constructor(
        private val dayRepository: DayRepository,
        private val taskRepository: TaskRepository
    ) : ViewModel() {
        private val _weeklyTask = MutableStateFlow<List<Task>>(emptyList())
        val weeklyTask = _weeklyTask.asStateFlow()

        private val today = LocalDate.now()
        private val startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        private val endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

        fun getWeeklyTask() = viewModelScope.launch {
            val startDate = localDateToDate(startOfWeek)
            val endDate = localDateToDate(endOfWeek)
            val weeklyTasks = taskRepository.getWeeklyTask(startDate, endDate)
            if(weeklyTasks.isNotEmpty()){
                _weeklyTask.value = weeklyTasks
            }
        }



        private fun localDateToDate(date: LocalDate): Date = Date.from(date.atStartOfDay(ZoneOffset.UTC).toInstant())
    }
