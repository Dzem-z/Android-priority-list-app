package com.example.prioritylist.data.backend

import androidx.annotation.VisibleForTesting
import com.example.prioritylist.data.backend.Status
import com.example.prioritylist.data.backend.StatusCodes
import com.example.prioritylist.data.database.ListEntity
import com.example.prioritylist.data.database.ListRepository
import com.example.prioritylist.data.database.TaskEntity
import java.lang.Math.sqrt
import java.util.Collections.list
import java.util.Collections.max
import java.util.Date
import kotlin.math.*

/*
TODO(comments)
 */

abstract class TaskList<TaskType: Task>(
    protected var name: String,
    protected var id: Int,
    protected var dateOfCreation: Date,
    protected val listRepository: ListRepository,
    protected val listOfTasks: MutableList<TaskType> = mutableListOf<TaskType>(),
    protected val historyTasks: MutableList<HistoryTask<TaskType>>
) {

    var history: HistoryList<TaskType>

    init{
        history = HistoryList(listRepository, this, historyTasks)
    }

    protected open val storage: Storage<TaskType> = Storage<TaskType>()

    abstract fun getPriority(id: Int): Double
    abstract fun toTaskEntity(task: TaskType): TaskEntity


    @VisibleForTesting
    suspend internal open fun add(task: TaskType): Status {
        if (listOfTasks.find{ task.name == it.name  } != null){
            return Status(StatusCodes.DUPLICATED_TASK)
        } else if (task.name.isEmpty()) {
            return Status(StatusCodes.EMPTY_NAME)
        }
        task.id = listOfTasks.size
        listOfTasks.add(task)
        sort()
        listRepository.add(toTaskEntity(task))
        return Status(StatusCodes.SUCCESS)
    }
    @VisibleForTesting
    suspend internal open fun delete(task: TaskType): Status {
        if (listOfTasks.find { it == task } == null){
            throw NoSuchElementException()
        }
        listOfTasks.remove(task)
        normalizeIndexes()
        listRepository.delete(toTaskEntity(task))
        return Status(StatusCodes.SUCCESS)
    }

    @JvmName("getNameTaskList")
    fun getName(): String {
        return name
    }
    fun getID(): Int {
        return id
    }
    protected fun sort() {
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
    suspend fun changeID(newID: Int) {
        id = newID
    }

    @JvmName("getDateOfCreationTaskList")
    fun getDateOfCreation(): Date {
        return dateOfCreation
    }

    fun getList(): MutableList<TaskType> {
        sort()
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
    suspend fun undo(): Status {
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
    suspend fun editTask(id: Int, newTask: TaskType): Status {
        val oldTask = getTaskByID(id)
        val status = delete(oldTask)
        if (status.code != StatusCodes.SUCCESS)
            return status
        storage.push(Edit(oldTask, newTask))
        return add(newTask)
    }

    suspend fun deleteTask(deletedTask: TaskType): Status {
        val status = delete(deletedTask)
        if (status.code == StatusCodes.SUCCESS)
            storage.push(Delete(deletedTask))
        return status
    }

    suspend fun addTask(newTask: TaskType): Status {
        storage.push(Add(newTask))
        return add(newTask)
    }


}

class CategoryTaskList(
    name: String,
    id: Int,
    dateOfCreation: Date,
    listRepository: ListRepository,
    listOfTasks: MutableList<CategoryTask> = mutableListOf(),
    historyTasks: MutableList<HistoryTask<CategoryTask>> = mutableListOf()
): TaskList<CategoryTask>(
    name = name,
    id = id,
    dateOfCreation = dateOfCreation,
    listRepository = listRepository,
    listOfTasks = listOfTasks,
    historyTasks = historyTasks
) {

    override fun toTaskEntity(task: CategoryTask): TaskEntity {
        TODO("Not yet implemented")
    }

    override fun getPriority(id: Int): Double {
        TODO("not yet implemented")
    }
}

class DeadlineTaskList(
    name: String,
    id: Int,
    dateOfCreation: Date,
    listRepository: ListRepository,
    listOfTasks: MutableList<DeadlineTask> = mutableListOf(),
    historyTasks: MutableList<HistoryTask<DeadlineTask>> = mutableListOf()
): TaskList<DeadlineTask>(
    name = name,
    id = id,
    dateOfCreation = dateOfCreation,
    listRepository = listRepository,
    listOfTasks = listOfTasks,
    historyTasks = historyTasks
) {

    private var maximumDeadline: Long = Long.MIN_VALUE

    init {
        maximumDeadline = super.listOfTasks.map { it.deadline.toInstant().toEpochMilli() }.maxOrNull() ?: Long.MIN_VALUE
    }

    override fun toTaskEntity(task: DeadlineTask): TaskEntity {
        return TaskEntity(
            name = task.name,
            description = task.description,
            dateOfCreation = task.dateOfCreation,
            priority = null,
            category = null,
            deadline = task.deadline,
            listID = id,
            type = TaskTypes.DEADLINE,
            dateOfCompletion = null
        )
    }

    override suspend fun add(task: DeadlineTask): Status {
        maximumDeadline = max(
            super.listOfTasks.map { it.deadline.toInstant().toEpochMilli() }.maxOrNull() ?: Long.MIN_VALUE,
            task.deadline.toInstant().toEpochMilli())
        return super.add(task)
    }

    override suspend fun delete(task: DeadlineTask): Status {
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
    name: String,
    id: Int,
    dateOfCreation: Date,
    listRepository: ListRepository,
    listOfTasks: MutableList<PriorityTask> = mutableListOf(),
    historyTasks: MutableList<HistoryTask<PriorityTask>> = mutableListOf()
): TaskList<PriorityTask>(
    name = name,
    id = id,
    dateOfCreation = dateOfCreation,
    listRepository = listRepository,
    listOfTasks = listOfTasks,
    historyTasks = historyTasks
) {
    private var maximumPriority = Int.MIN_VALUE

    init {
        maximumPriority = super.listOfTasks.map { it.priority }.maxOrNull() ?: Int.MIN_VALUE
    }

    override fun toTaskEntity(task: PriorityTask): TaskEntity {
        return TaskEntity(
            name = task.name,
            description = task.description,
            dateOfCreation = task.dateOfCreation,
            priority = task.priority,
            category = null,
            deadline = null,
            listID = id,
            type = TaskTypes.PRIORITY,
            dateOfCompletion = null
        )
    }

    override suspend fun add(task: PriorityTask): Status {
        val status = super.add(task)
        if (status.code == StatusCodes.SUCCESS)
            maximumPriority = max(
                super.listOfTasks.map { it.priority }.maxOrNull() ?: Int.MIN_VALUE,
                task.priority)
        return status
    }

    override suspend fun delete(task: PriorityTask): Status {
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
    name: String,
    id: Int,
    dateOfCreation: Date,
    listRepository: ListRepository,
    listOfTasks: MutableList<DeadlineCategoryTask> = mutableListOf(),
    historyTasks: MutableList<HistoryTask<DeadlineCategoryTask>> = mutableListOf()
): TaskList<DeadlineCategoryTask>(
    name = name,
    id = id,
    dateOfCreation = dateOfCreation,
    listRepository = listRepository,
    listOfTasks = listOfTasks,
    historyTasks = historyTasks
) {
    override fun toTaskEntity(task: DeadlineCategoryTask): TaskEntity {
        TODO("Not yet implemented")
    }

    override fun getPriority(id: Int): Double {
        TODO("not yet implemented")
    }
}

class DeadlinePriorityTaskList(
    name: String,
    id: Int,
    dateOfCreation: Date,
    listRepository: ListRepository,
    listOfTasks: MutableList<DeadlinePriorityTask> = mutableListOf(),
    historyTasks: MutableList<HistoryTask<DeadlinePriorityTask>> = mutableListOf()
): TaskList<DeadlinePriorityTask>(
    name = name,
    id = id,
    dateOfCreation = dateOfCreation,
    listRepository = listRepository,
    listOfTasks = listOfTasks,
    historyTasks = historyTasks
) {
    override fun toTaskEntity(task: DeadlinePriorityTask): TaskEntity {
        TODO("Not yet implemented")
    }

    override fun getPriority(id: Int): Double {
        TODO("not yet implemented")
    }
}

class DeadlinePriorityCategoryTaskList(
    name: String,
    id: Int,
    dateOfCreation: Date,
    listRepository: ListRepository,
    listOfTasks: MutableList<DeadlinePriorityCategoryTask> = mutableListOf(),
    historyTasks: MutableList<HistoryTask<DeadlinePriorityCategoryTask>> = mutableListOf()
): TaskList<DeadlinePriorityCategoryTask>(
    name = name,
    id = id,
    dateOfCreation = dateOfCreation,
    listRepository = listRepository,
    listOfTasks = listOfTasks,
    historyTasks = historyTasks
) {
    override fun toTaskEntity(task: DeadlinePriorityCategoryTask): TaskEntity {
        TODO("Not yet implemented")
    }

    override fun getPriority(id: Int): Double {
        TODO("not yet implemented")
    }
}