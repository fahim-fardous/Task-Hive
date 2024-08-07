package com.example.taskhive.components

import androidx.room.TypeConverter
import com.example.taskhive.domain.model.Project
import com.example.taskhive.domain.model.Task
import com.google.gson.Gson
import java.util.Date

class DataTypeConverters {
    @TypeConverter
    fun fromTimeStamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimeStamp(date: Date?): Long? = date?.time

    @TypeConverter
    fun fromProject(project: Project?): String {
        return Gson().toJson(project)
    }

    @TypeConverter
    fun toProject(json: String): Project? {
        return Gson().fromJson(json, Project::class.java)
    }

    @TypeConverter
    fun fromList(list: List<Task>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toList(json: String): List<Task> {
        return Gson().fromJson(json, Array<Task>::class.java).toList()
    }
}
