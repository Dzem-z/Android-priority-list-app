package com.example.prioritylist.data.database

import androidx.room.TypeConverter
import java.time.Instant
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date.from(Instant.ofEpochSecond(value)) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.toInstant()?.epochSecond
    }
}