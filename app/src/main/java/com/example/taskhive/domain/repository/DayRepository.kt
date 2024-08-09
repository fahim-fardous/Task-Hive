package com.example.taskhive.domain.repository

import com.example.taskhive.domain.model.Day
import java.util.Date

interface DayRepository {
    suspend fun saveDay(day: Day): Long

    suspend fun getAllDays(): List<Day>

    suspend fun getDay(date: Date): Day
}
