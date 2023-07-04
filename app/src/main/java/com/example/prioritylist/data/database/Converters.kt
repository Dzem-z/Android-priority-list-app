package com.example.prioritylist.data.database

import androidx.room.TypeConverter
import com.example.prioritylist.data.backend.TaskTypes
import java.time.Instant
import java.util.Date

/**
 * type converters used by [database]
 */

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date.from(Instant.ofEpochSecond(value)) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.toInstant()?.epochSecond
    }

    @TypeConverter
    fun typeToInt(type: TaskTypes?): Int? {
        return when(type){
            TaskTypes.PRIORITY -> 1
            TaskTypes.DEADLINE -> 2
            TaskTypes.CATEGORY -> 3
            TaskTypes.DEADLINE_PRIORITY -> 4
            TaskTypes.DEADLINE_CATEGORY -> 5
            TaskTypes.DEADLINE_PRIORITY_CATEGORY -> 6
            else -> null
        }
    }

    @TypeConverter
    fun intToType(int: Int?): TaskTypes? {
        return when(int){
            1 -> TaskTypes.PRIORITY
            2 -> TaskTypes.DEADLINE
            3 -> TaskTypes.CATEGORY
            4 -> TaskTypes.DEADLINE_PRIORITY
            5 -> TaskTypes.DEADLINE_CATEGORY
            6 -> TaskTypes.DEADLINE_PRIORITY_CATEGORY
            else -> null
        }
    }
}