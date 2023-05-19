package com.example.prioritylist.data

import androidx.annotation.VisibleForTesting
import java.lang.Math.sqrt
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
        if (listOfTasks.find{ task.name == it.name  } != null){
            return Status(StatusCodes.DUPLICATED_TASK)
        } else if (task.name.isEmpty()) {
            return Status(StatusCodes.EMPTY_NAME)
        }
        task.id = listOfTasks.size
        listOfTasks.add(task)
        sort()
        //TODO(write to database)
        return Status(StatusCodes.SUCCESS)
    }
    @VisibleForTesting
    internal open fun delete(task: TaskType): Status {
        if (listOfTasks.find { it == task } == null){
            throw NoSuchElementException()
        }
        listOfTasks.remove(task)
        normalizeIndexes()
        //TODO(write to database)
        return Status(StatusCodes.SUCCESS)
    }

    @JvmName("getNameTaskList")
    fun getName(): String {
        return name
    }
    fun getID(): Int {
        return id
    }
    private fun sort() {
        for(i in listOfTasks) {
            getPriority(i.id)
        }

        listOfTasks.sortBy{ it.evaluatedPriority }
        listOfTasks.reverse()
        normalizeIndexes()
    }

    private fun normalizeIndexes() {
        listOfTasks.forEachIndexed { index, element -> element.id = index }
    }

    fun changeName(newName: String){
        name = newName
    }
    fun changeID(newID: Int) {
        id = newID
    }
    fun getList(): MutableList<TaskType> {
        return listOfTasks.toMutableList()
    }
    fun getTaskByName(name: String): TaskType {
        val element = listOfTasks.find { it.name == name }
        if (element != null)
            return element
        else
            throw NoSuchElementException()
    }
    fun getTaskByID(id: Int): TaskType {
        return listOfTasks[id]
    }
    fun undo() {
        TODO("Not yet implemented")
    }

    fun updatePriority() {
        TODO("Not yet implemented")
    }
    fun editTask(id: Int, newTask: TaskType): Status {
        //TODO("push storage action")
        val status = delete(getTaskByID(id))
        if(status.code != StatusCodes.SUCCESS)
            return status
        return add(newTask)
    }

    fun deleteTask(deletedTask: TaskType): Status {
        //TODO("push storage action")
        return delete(deletedTask)
    }

    fun addTask(newTask: TaskType): Status {
        //TODO("push storage action")
        return add(newTask)
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

    private var maximumDeadline: Long = Long.MIN_VALUE

    override fun add(task: DeadlineTask): Status {
        maximumDeadline = max(
            super.listOfTasks.map { it.deadline.toInstant().toEpochMilli() }.maxOrNull() ?: Long.MIN_VALUE,
            task.deadline.toInstant().toEpochMilli())
        return super.add(task)
    }

    override fun delete(task: DeadlineTask): Status {
        maximumDeadline = max(
            super.listOfTasks.map { it.deadline.toInstant().toEpochMilli() }.maxOrNull() ?: Long.MIN_VALUE,
            task.deadline.toInstant().toEpochMilli())
        return super.delete(task)
    }

    override fun getPriority(id: Int): Double {
        val currentTask = super.listOfTasks[id]
        val dateInt = currentTask.deadline.toInstant().toEpochMilli()

        currentTask.evaluatedPriority = sqrt(1 - dateInt  * 1.0/maximumDeadline) * 100
        //evaluates priority: scales between 0 - 100 asymptotically to root function
        return currentTask.evaluatedPriority
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
        val status = super.add(task)
        if (status.code == StatusCodes.SUCCESS)
            maximumPriority = max(
                super.listOfTasks.map { it.priority }.maxOrNull() ?: Int.MIN_VALUE,
                task.priority)
        return status
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

        currentTask.evaluatedPriority = sqrt(currentPriority * 1.0/maximumPriority ) * 100
        //evaluates priority: scales between 0 - 100 asymptotically to root function
        return currentTask.evaluatedPriority
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