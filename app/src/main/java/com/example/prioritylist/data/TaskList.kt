package com.example.prioritylist.data

import androidx.annotation.VisibleForTesting
import java.lang.Math.sqrt
import java.time.LocalDateTime
import java.util.Collections.max


/*
TODO(comments)
 */

abstract class TaskList<TaskType: Task>(
    protected open var name: String,
    protected open var id: Int
) {



    protected open val listOfTasks: MutableList<TaskType> = mutableListOf<TaskType>()
    protected open val storage: Storage<TaskType> = Storage<TaskType>()
    protected open val history: HistoryList<TaskType> = HistoryList<TaskType>()
    abstract fun getPriority(id: Int): Double

    @VisibleForTesting
    internal open fun add(task: TaskType): Status {
        TODO("Not yet implemented")
    }
    @VisibleForTesting
    internal open fun delete(task: TaskType): Status {
        TODO("Not yet implemented")
    }

    @JvmName("getNameTaskList")
    fun getName(): String {
        TODO("Not yet implemented")
    }
    fun getID(): Int {
        TODO("Not yet implemented")
    }
    fun sort() {
        TODO("Not yet implemented")
    }
    fun changeName(newName: String){
        TODO("Not yet implemented")
    }
    fun changeID(newID: Int) {
        TODO("Not yet implemented")
    }
    fun getList(): MutableList<TaskType> {
        TODO("Not yet implemented")
    }
    fun getTaskByName(name: String): TaskType {
        TODO("Not yet implemented")
    }
    fun getTaskByID(id: Int): TaskType {
        TODO("Not yet implemented")
    }
    fun deleteTask(deletedTask: TaskType): Status {
        TODO("Not yet implemented")
    }
    fun undo() {
        TODO("Not yet implemented")
    }
    fun moveToHistory(task: TaskType) {
        TODO("Not yet implemented")
    }
    fun updatePriority() {
        TODO("Not yet implemented")
    }
    fun editTask(id: Int, newTask: TaskType): Status {
        TODO("Not yet implemented")
    }
    fun addTask(newTask: TaskType): Status {
        TODO("Not yet implemented")
    }
}

class CategoryTaskList(
    override var name: String,
    override var id: Int,
): TaskList<CategoryTask>(
    name = name,
    id = id
) {



    override fun getPriority(id: Int): Double {
        TODO("not yet implemented")
    }
}

class DeadlineTaskList(
    override var name: String,
    override var id: Int,
): TaskList<DeadlineTask>(
    name = name,
    id = id
) {

    private var maximumDeadline: Int = 0

    private fun dateToInt(date: LocalDateTime): Int {
        return date.second + 60 * (date.minute + 60 * (date.hour + 24 * (date.dayOfYear + 365 * date.year)))
    }

    override fun add(task: DeadlineTask): Status {
        val status = super.add(task)
        maximumDeadline = max(super.listOfTasks.map { dateToInt(it.deadline) })
        return status
    }

    override fun delete(task: DeadlineTask): Status {
        val status = super.delete(task)
        maximumDeadline = max(super.listOfTasks.map { dateToInt(it.deadline) })
        return status
    }

    override fun getPriority(id: Int): Double {
        val currentTask = super.listOfTasks[id]
        val dateInt = dateToInt(currentTask.deadline)

        val priority = sqrt(dateInt/maximumDeadline * 1.0) * 100
        //evaluates priority: scales between 0 - 100 asymptotically to root function
        return priority
    }
}

class PriorityTaskList(
    override var name: String,
    override var id: Int,
): TaskList<PriorityTask>(
    name = name,
    id = id
) {
    override fun getPriority(id: Int): Double {
        TODO("not yet implemented")
    }
}

class DeadlineCategoryTaskList(
    override var name: String,
    override var id: Int
): TaskList<DeadlineCategoryTask>(
    name = name,
    id = id
) {
    override fun getPriority(id: Int): Double {
        TODO("not yet implemented")
    }
}

class DeadlinePriorityTaskList(
    override var name: String,
    override var id: Int
): TaskList<DeadlinePriorityTask>(
    name = name,
    id = id
) {
    override fun getPriority(id: Int): Double {
        TODO("not yet implemented")
    }
}

class DeadlinePriorityCategoryTaskList(
    override var name: String,
    override var id: Int
): TaskList<DeadlinePriorityCategoryTask>(
    name = name,
    id = id
) {
    override fun getPriority(id: Int): Double {
        TODO("not yet implemented")
    }
}