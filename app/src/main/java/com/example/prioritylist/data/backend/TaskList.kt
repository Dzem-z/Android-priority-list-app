package com.example.prioritylist.data.backend

import androidx.annotation.VisibleForTesting
import com.example.prioritylist.data.backend.Status
import com.example.prioritylist.data.backend.StatusCodes
import java.lang.Math.sqrt
import java.util.Collections.list
import java.util.Collections.max
import java.util.Date
import kotlin.math.*

/*
TODO(comments)
 */

abstract class TaskList<TaskType: Task>(
    protected open var name: String,
    protected open var id: Int,
    protected open var dateOfCreation: Date
) {


    protected open val listOfTasks: MutableList<TaskType> = mutableListOf<TaskType>()
    protected open val storage: Storage<TaskType> = Storage<TaskType>()
    open val history: HistoryList<TaskType> = HistoryList<TaskType>()
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

    @JvmName("getDateOfCreationTaskList")
    fun getDateOfCreation(): Date {
        return dateOfCreation
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
    fun undo(): Status {
        if (storage.isEmpty())
            throw Exception()
        else {
            val action = storage.pop()
            if(action is Add){
                return delete(action.oldTask)
            } else if (action is Edit) {
                val status = delete(getTaskByName(action.newTask.name))
                if (status.code != StatusCodes.SUCCESS)
                    return status
                return add(action.oldTask)
            } else if (action is Delete) {
                return add(action.oldTask)
            }
        }
        return Status(StatusCodes.FAILURE)
    }

    fun isStorageEmpty(): Boolean {
        return storage.isEmpty()
    }

    fun updatePriority() {
        TODO("Not yet implemented")
    }
    fun editTask(id: Int, newTask: TaskType): Status {
        val oldTask = getTaskByID(id)
        val status = delete(oldTask)
        if (status.code != StatusCodes.SUCCESS)
            return status
        storage.push(Edit(oldTask, newTask))
        return add(newTask)
    }

    fun deleteTask(deletedTask: TaskType): Status {
        val status = delete(deletedTask)
        if (status.code == StatusCodes.SUCCESS)
            storage.push(Delete(deletedTask))
        return status
    }

    fun addTask(newTask: TaskType): Status {
        storage.push(Add(newTask))
        return add(newTask)
    }
}

class CategoryTaskList(
    override var name: String,
    override var id: Int,
    override var dateOfCreation: Date
): TaskList<CategoryTask>(
    name = name,
    id = id,
    dateOfCreation = dateOfCreation
) {



    override fun getPriority(id: Int): Double {
        TODO("not yet implemented")
    }
}

class DeadlineTaskList(
    override var name: String,
    override var id: Int,
    override var dateOfCreation: Date
): TaskList<DeadlineTask>(
    name = name,
    id = id,
    dateOfCreation = dateOfCreation
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
    override var dateOfCreation: Date
): TaskList<PriorityTask>(
    name = name,
    id = id,
    dateOfCreation = dateOfCreation
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
    override var id: Int,
    override var dateOfCreation: Date
): TaskList<DeadlineCategoryTask>(
    name = name,
    id = id,
    dateOfCreation = dateOfCreation
) {
    override fun getPriority(id: Int): Double {
        TODO("not yet implemented")
    }
}

class DeadlinePriorityTaskList(
    override var name: String,
    override var id: Int,
    override var dateOfCreation: Date
): TaskList<DeadlinePriorityTask>(
    name = name,
    id = id,
    dateOfCreation = dateOfCreation
) {
    override fun getPriority(id: Int): Double {
        TODO("not yet implemented")
    }
}

class DeadlinePriorityCategoryTaskList(
    override var name: String,
    override var id: Int,
    override var dateOfCreation: Date
): TaskList<DeadlinePriorityCategoryTask>(
    name = name,
    id = id,
    dateOfCreation = dateOfCreation
) {
    override fun getPriority(id: Int): Double {
        TODO("not yet implemented")
    }
}