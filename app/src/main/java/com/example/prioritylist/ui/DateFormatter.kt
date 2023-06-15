package com.example.prioritylist.ui

import java.util.Calendar
import java.util.Date

/*
* function that formats Date to format more friendly to user
* */

fun dateFormatter(date: Date): String {
    val cal = Calendar.getInstance()
    cal.time = date
    return getDayOfWeek(cal.get(Calendar.DAY_OF_WEEK)) + " " +
            getMonth(cal.get(Calendar.MONTH)) + " " + cal.get(Calendar.YEAR).toString() + " " +
            cal.get(Calendar.DAY_OF_MONTH).toString()
}

internal fun getDayOfWeek(day: Int): String{
    return when(day) {
        1 -> "Monday"
        2 -> "Tuesday"
        3 -> "Wednesday"
        4 -> "Thursday"
        5 -> "Friday"
        6 -> "Saturday"
        7 -> "Sunday"
        else -> "error"
    }
}

internal fun getMonth(month: Int): String{
    return when(month) {
        1 -> "January"
        2 -> "February"
        3 -> "March"
        4 -> "April"
        5 -> "May"
        6 -> "June"
        7 -> "July"
        8 -> "August"
        9 -> "September"
        10 -> "October"
        11 -> "November"
        12 -> "December"
        else -> "error"
    }
}