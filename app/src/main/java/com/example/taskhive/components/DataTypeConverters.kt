package com.example.taskhive.components

import androidx.room.TypeConverter
import java.util.Date

class DataTypeConverters {
    @TypeConverter
    fun fromTimeStamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimeStamp(date: Date?): Long? = date?.time
}
