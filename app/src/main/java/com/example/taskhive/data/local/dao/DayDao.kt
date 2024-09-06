package com.example.taskhive.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.taskhive.domain.model.Day
import java.util.Date

@Dao
interface DayDao {
    @Upsert
    suspend fun addDay(day: Day): Long

    @Query("SELECT * FROM days WHERE date = :date")
    suspend fun getDay(date: Date): Day

    @Query("SELECT * FROM days")
    suspend fun getDays(): List<Day>

    @Query("SELECT * FROM days WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getWeeklyReportByDay(
        startDate: Date,
        endDate: Date,
    ): List<Day>
}
