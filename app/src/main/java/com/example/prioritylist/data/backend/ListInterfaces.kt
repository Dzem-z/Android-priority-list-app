package com.example.prioritylist.data.backend

import java.lang.Integer.max
import java.lang.Integer.min
import kotlin.math.sqrt
import java.util.Calendar
import java.util.concurrent.TimeUnit

interface DeadlineHandlerInterface {

    fun getPriority(deadlineInMillis: Long): Double
    fun doesDeadlinePassed(): Boolean
    fun getDeadline(): Long
    fun getFactor(): Int
    fun getDate(): Long
}

class DeadlineHandlerImpl: DeadlineHandlerInterface {
    var deadlineOfTask: Long = Long.MAX_VALUE
    var today: Long = Long.MAX_VALUE
    var scalingFactor: Int = Period.SHORT_PERIOD.value  //scaling variable used in interpolation function

    companion object {
        //hardcoded values used to determine the right scaling for the list (user may want to rather put long deadlines than short and other way round)
        enum class Period(val value: Int) {
            SHORT_PERIOD(20),
            MEDIUM_PERIOD(90),
            LONG_PERIOD(300)
        }
    }

    init {
        today = TimeUnit.DAYS.convert(Calendar.getInstance().timeInMillis, TimeUnit.MILLISECONDS)
    }

    private fun interpolatingFunction(x: UInt): Double {   //takes x as number of days to the specified deadline
        return MAXIMUM_PRIORITY * scalingFactor * 1.0 / (x.toInt() * x.toInt() + scalingFactor - 1)
    }

    override fun getPriority(deadlineInMillis: Long): Double {
        deadlineOfTask = TimeUnit.DAYS.convert(deadlineInMillis, TimeUnit.MILLISECONDS)
        today = TimeUnit.DAYS.convert(Calendar.getInstance().timeInMillis, TimeUnit.MILLISECONDS)
        return if (doesDeadlinePassed()) {
            150.0
        } else {
            interpolatingFunction((deadlineOfTask - today).toUInt())
        }
    }

    override fun doesDeadlinePassed(): Boolean {
        return deadlineOfTask < today
    }

    override fun getDate(): Long {
        return today
    }

    override fun getDeadline(): Long {
        return deadlineOfTask
    }

    override fun getFactor(): Int {
        return scalingFactor
    }
}

interface RelativePriorityHandlerInterface {
    fun updateExtremes(minPriority: Int, maxPriority: Int)
    fun getPriority(currentPriority: Int): Double
}

class RelativePriorityHandlerImpl : RelativePriorityHandlerInterface {
    var maximumPriority: Int = Int.MIN_VALUE
    var minimumPriority: Int = Int.MAX_VALUE


    override fun updateExtremes(minPriority: Int, maxPriority: Int) {
        minimumPriority = min(minPriority, minimumPriority)
        maximumPriority = max(maxPriority, maximumPriority)
    }

    override fun getPriority(currentPriority: Int): Double {
        return interpolatingFunction(currentPriority, minimumPriority, maximumPriority)
    }

    private fun interpolatingFunction(x: Int, min: Int, max: Int): Double { //stretches linear function on maximum and minimum priority, then picks point according to x priority
        return sqrt((x - min) * 1.0 / (max - min)) * MAXIMUM_PRIORITY
    }


}

interface AbsolutePriorityHandlerInterface {
    fun getPriority(currentPriority: Int): Double
}

class AbsolutePriorityHandlerImpl : AbsolutePriorityHandlerInterface {

    var scalingFactor: Int = PriorityRange.MEDIUM_RANGE.value  //scaling variable used in getPriority

    companion object {
        //hardcoded values used to determine the right scaling for the list (1 to 10 or 1 to 100..)
        enum class PriorityRange(val value: Int) {
            SHORT_RANGE(10),
            MEDIUM_RANGE(100),
            LONG_RANGE(1000)
        }
    }

    override fun getPriority(currentPriority: Int): Double {
        return if(currentPriority in 1..scalingFactor)
            currentPriority * 1.0 / scalingFactor * MAXIMUM_PRIORITY
        else
            1.0
    }
}

interface DeadlinePriorityHandlerInterface{
    fun getPriority(priority: Int, deadlineInMillis: Long): Double
}

class DeadlinePriorityHandlerImpl(
    priorityHandler: AbsolutePriorityHandlerInterface = AbsolutePriorityHandlerImpl(),
    deadlineHandler: DeadlineHandlerInterface = DeadlineHandlerImpl()
) : AbsolutePriorityHandlerInterface by priorityHandler, DeadlineHandlerInterface by deadlineHandler, DeadlinePriorityHandlerInterface {


    private fun interpolatingFunction(priority: Int, deadline: Long): Double {  //scales inversely proportional to priority and deadline as well
        return MAXIMUM_PRIORITY * getFactor() * 1.0 / ((MAXIMUM_PRIORITY - priority) * deadline + getFactor() - 1)
    }

    override fun getPriority(priority: Int, deadlineInMillis: Long): Double {
        getPriority(deadlineInMillis)   //update dates

        return interpolatingFunction(priority, getDeadline())
    }


}