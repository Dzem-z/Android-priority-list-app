package com.example.prioritylist.ui

import java.util.Calendar
import java.util.Date

/**
* [dateFormatter] is a function that formats Date to format more friendly to user
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
        Calendar.MONDAY -> "Monday"
        Calendar.TUESDAY -> "Tuesday"
        Calendar.WEDNESDAY -> "Wednesday"
        Calendar.THURSDAY -> "Thursday"
        Calendar.FRIDAY -> "Friday"
        Calendar.SATURDAY -> "Saturday"
        Calendar.SUNDAY -> "Sunday"
        else -> "error"
    }
}

internal fun getMonth(month: Int): String{
    return when(month) {
        Calendar.JANUARY -> "January"
        Calendar.FEBRUARY -> "February"
        Calendar.MARCH -> "March"
        Calendar.APRIL -> "April"
        Calendar.MAY -> "May"
        Calendar.JUNE -> "June"
        Calendar.JULY -> "July"
        Calendar.AUGUST -> "August"
        Calendar.SEPTEMBER -> "September"
        Calendar.OCTOBER -> "October"
        Calendar.NOVEMBER -> "November"
        Calendar.DECEMBER -> "December"
        else -> "error"
    }
}