package com.example.taskhive.data

import com.example.taskhive.data.local.AppDatabase
import com.example.taskhive.domain.model.Day
import com.example.taskhive.domain.repository.DayRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

class DayRepositoryImp
    @Inject
    constructor(
        private val db: AppDatabase,
    ) : DayRepository {
        override suspend fun saveDay(day: Day): Long =
            withContext(Dispatchers.IO) {
                db.dayDao().addDay(day)
            }

        override suspend fun getAllDays(): List<Day> =
            withContext(Dispatchers.IO) {
                db.dayDao().getDays()
            }

        override suspend fun getDay(date: Date): Day =
            withContext(Dispatchers.IO) {
                db.dayDao().getDay(date)
            }

        override suspend fun getWeeklyReportByDay(
            startDate: Date,
            endDate: Date,
        ): List<Day> =
            withContext(Dispatchers.IO) {
                db.dayDao().getWeeklyReportByDay(startDate, endDate)
            }
    }
