package com.example.taskhive.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.TypeConverter

class TypeConverters {
    @TypeConverter
    fun fromColor(color: Color):Int{
        return color.toArgb()
    }

    @TypeConverter
    fun toColor(colorInt: Int):Color{
        return Color(colorInt)
    }

}