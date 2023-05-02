package com.example.prioritylist.data

import androidx.annotation.VisibleForTesting
import java.lang.Math.sqrt
import java.time.LocalDateTime
import java.util.Collections.list
import java.util.Collections.max
import kotlin.math.*

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
        task.id = listOfTasks.size
        listOfTasks.add(task)
        sort()
        //TODO(write to database)
        return Status(StatusEnum.SUCCESS)
    }
    @VisibleForTesting
    internal open fun delete(task: TaskType): Status {
        TODO("Not yet implemented")
    }

    internal fun dateToInt(date: LocalDateTime): Int {
        return date.minute + 60 * (date.hour + 24 * (date.dayOfYear + 365 * date.year))
    }

    @JvmName("getNameTaskList")
    fun getName(): String {
        TODO("Not yet implemented")
    }
    fun getID(): Int {
        TODO("Not yet implemented")
    }
    private fun sort() {
        listOfTasks.sortWith(compareBy<Task> { getPriority(it.id) }.
        thenBy { -dateToInt(it.dateOfCreation) })
        listOfTasks.reverse()
        listOfTasks.forEachIndexed { index, element -> element.id = index }
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
    fun getTaskByName(name: String): TaskType? {

        return listOfTasks.find { it.name == name }
    }
    fun getTaskByID(id: Int): TaskType {
        return listOfTasks[id]
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

    private var maximumDeadline: Int = Int.MIN_VALUE

    override fun add(task: DeadlineTask): Status {
        maximumDeadline = max(
            super.listOfTasks.map { dateToInt(it.deadline) }.maxOrNull() ?: Int.MIN_VALUE,
            dateToInt(task.deadline))
        return super.add(task)
    }

    override fun delete(task: DeadlineTask): Status {
        maximumDeadline = max(
            super.listOfTasks.map { dateToInt(it.deadline) }.maxOrNull() ?: Int.MIN_VALUE,
            dateToInt(task.deadline))
        return super.delete(task)
    }

    override fun getPriority(id: Int): Double {
        val currentTask = super.listOfTasks[id]
        val dateInt = dateToInt(currentTask.deadline)

        val priority = sqrt(1 - dateInt  * 1.0/maximumDeadline) * 100
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
    private var maximumPriority = Int.MIN_VALUE

    override fun add(task: PriorityTask): Status {
        maximumPriority = max(
            super.listOfTasks.map { it.priority }.maxOrNull() ?: Int.MIN_VALUE,
            task.priority)
        return super.add(task)
    }

    override fun delete(task: PriorityTask): Status {
        maximumPriority = max(
            super.listOfTasks.map { it.priority }.maxOrNull() ?: Int.MIN_VALUE,
            task.priority)
        return super.delete(task)
    }

    override fun getPriority(id: Int): Double {
        val currentTask = super.listOfTasks[id]
        val currentPriority = currentTask.priority

        val priority = sqrt(currentPriority * 1.0/maximumPriority ) * 100
        //evaluates priority: scales between 0 - 100 asymptotically to root function
        return priority
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