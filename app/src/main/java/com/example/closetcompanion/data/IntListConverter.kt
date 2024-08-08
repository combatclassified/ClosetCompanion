package com.example.closetcompanion.data

import androidx.room.TypeConverter

class IntListConverter {
    @TypeConverter
    fun fromList(list: List<Int>): String {
        return list.joinToString(separator = ",")
    }

    @TypeConverter
    fun toList(string: String): List<Int> {
        if( string == "")
            return emptyList();
        return string.split(",").map { it.toInt() }
    }
}