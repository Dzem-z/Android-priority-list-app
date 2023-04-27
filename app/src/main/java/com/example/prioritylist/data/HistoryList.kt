package com.example.prioritylist.data

import java.time.LocalDateTime
import java.util.Date

/*
TODO(comments)
 */


class HistoryList<TaskType: Task>() {
    val listOfTasks: MutableList<TaskType> = mutableListOf<TaskType>()

    fun addTask(newTask: TaskType) {
        TODO("not yet implemented")
    }
    fun getTaskByID(id: Int): HistoryTask<TaskType> {
        TODO("not yet implemented")
    }
    fun getList(): MutableList<HistoryTask<TaskType>> {
        TODO("not yet implemented")
    }
    fun deleteTask(deletedTask: HistoryTask<TaskType>) {
        TODO("not yet implemented")
    }
    fun deleteUntil(date: LocalDateTime) {
        TODO("not yet implemented")
    }

}

data class HistoryTask<TaskType: Task>(
    val it: TaskType,
    val completionDate: Date
)