package com.example.prioritylist.data.backend

import androidx.annotation.VisibleForTesting
import com.example.prioritylist.data.backend.Status
import com.example.prioritylist.data.backend.StatusCodes
import com.example.prioritylist.data.database.ListEntity
import com.example.prioritylist.data.database.ListRepository
import com.example.prioritylist.data.database.TaskEntity
import java.lang.Math.sqrt
import java.util.Calendar
import java.util.Collections.list
import java.util.Collections.max
import java.util.Date
import java.util.concurrent.TimeUnit
import kotlin.math.*

/**
 * [TaskList] is an abstract container class used for storing [Task] objects as well as managing [Storage]
 * and providing access to [HistoryList]
 *
 * @param name is a name of a list
 * @param id is a id of the list
 * @param dateOfCreation is a date of creation of a list
 * @param listRepository is a reference to the listRepository
 * @param listOfTasks is a list of TaskType that holds every task of list being created
 * @param historyTasks is a list of every [HistoryTask] that belongs to the [HistoryList] associated with the list
 */

val MAXIMUM_PRIORITY = 100  //maximum numerical value possible holded by task.evaluatedPriority

abstract class TaskList<TaskType: Task>(
    protected var name: String,
    protected var id: Int,
    protected var dateOfCreation: Date,
    protected val listRepository: ListRepository,
    protected val listOfTasks: MutableList<TaskType> = mutableListOf<TaskType>(),
    protected val historyTasks: MutableList<HistoryTask<TaskType>>
) {

    var history: HistoryList<TaskType>  //reference to HistoryList

    init{
        history = HistoryList(listRepository, this, historyTasks)   //creates historyList and initializes it with tasks stored in the database
    }

    protected open val storage: Storage<TaskType> = Storage<TaskType>() //reference to storage

    /**
     * [getPriority] returns numerical value that indicates current priority of the task identified by id. The higher the value the bigger the priority is
     * @param id is an id of task
     */
    abstract fun getPriority(id: Int): Double

    /**
     * [toTaskEntity] converts [Task] to [TaskEntity]
     * @param task of [Task] type to be converted
     */
    abstract fun toTaskEntity(task: TaskType): TaskEntity

    /**
     * [add] is an internal method called when task is being added to the list.
     * It checks for duplicated task and empty name, inserts task to the list, and writes changes to the database.
     * Returns proper [Status] object.
     * @param task is a task of [Task] type that is being added
     */

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

    /**
     * [delete] is an internal method called when task is being deleted from the list.
     * It checks if the task is present in the list, removes task from the list, and writes changes to the database.
     * Returns proper [Status] object.
     * @param task is a task of [Task] type that is being deleted
     */

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

    /**
     * [getName] returns the name of the list
     */

    @JvmName("getNameTaskList")
    fun getName(): String {
        return name
    }

    /**
     * [getID] returns id of the list
     */

    fun getID(): Int {
        return id
    }

    /**
     * [sort] sorts tasks by its priority. It is used when task is added to the list (or edited).
     */

    protected fun sort() {
        for(i in listOfTasks) {
            getPriority(i.id)
        }

        listOfTasks.sortBy{ it.evaluatedPriority }
        listOfTasks.reverse()
        normalizeIndexes()
    }

    /**
     * [normalizeIndexes] assigns an id equal to the task index in the list to each task.
     */

    private fun normalizeIndexes() {
        listOfTasks.forEachIndexed { index, element -> element.id = index }
    }

    /**
     * [changeName] sets the list name to the newName
     * @param newName is the new name of the list
     */

    fun changeName(newName: String){
        name = newName
    }

    /**
     * [changeID] sets the list id to the newID
     * @param newID is the new id of the list
     */
    suspend fun changeID(newID: Int) {
        id = newID
    }

    /**
     * [getDateOfCreation] returns dateOfCreation
     */

    @JvmName("getDateOfCreationTaskList")
    fun getDateOfCreation(): Date {
        return dateOfCreation
    }

    /**
     * [getList] returns [MutableList] of every task in the list, sorted by priority (id).
     */

    fun getList(): MutableList<TaskType> {
        sort()
        return listOfTasks.toMutableList()
    }

    /**
     * [getTaskByName] returns [Task] identified by name
     * @param name is a name of the task
     *
     * if task with name is not present in the list, [NoSuchElementException] is thrown.
     */

    fun getTaskByName(name: String): TaskType {
        val element = listOfTasks.find { it.name == name }
        if (element != null)
            return element
        else
            throw NoSuchElementException()
    }

    /**
     * [getTaskByID] returns [Task] identified by id
     * @param id is an id of the task
     *
     * if task with id is not present in the list, [IndexOutOfBoundsException] is thrown.
     */

    fun getTaskByID(id: Int): TaskType {
        return listOfTasks[id]
    }

    /**
     * [undo] is a method that rewerts last action (addTask, deleteTask, editTask).
     * if there is no action to rewert, throws [Exception]
      */

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

    /**
     * [isStorageEmpty] returns true if storage is empty, false otherwise
     */

    fun isStorageEmpty(): Boolean {
        return storage.isEmpty()
    }

    /**
     *
     */

    fun updatePriority() {
        TODO("Not yet implemented")
    }

    /**
     * [editTask] replaces the task with id: id to newTask.
     * @param id is an id of the task to be replaced
     * @param newTask is the task to replace old task
     */

    suspend fun editTask(id: Int, newTask: TaskType): Status {
        val oldTask = getTaskByID(id)
        val status = delete(oldTask)
        if (status.code != StatusCodes.SUCCESS)
            return status
        storage.push(Edit(oldTask, newTask))
        return add(newTask)
    }

    /**
     * [deleteTask] deletes the task from the list
     * @param taskToDelete is the task to be deleted
     */

    suspend fun deleteTask(taskToDelete: TaskType): Status {
        val status = delete(taskToDelete)
        if (status.code == StatusCodes.SUCCESS)
            storage.push(Delete(taskToDelete))
        return status
    }

    /**
     * [addTask] adds the task to the list
     * @param newTask is the task to be added
     */

    suspend fun addTask(newTask: TaskType): Status {
        storage.push(Add(newTask))
        return add(newTask)
    }


}

/**
 * [CategoryTaskList] ranks tasks by category. Every task has assigned category, every category has its own priority and color.
 */

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

/**
 * [DeadlineTaskList] ranks tasks by deadline. the earlier the date, the higher the priority is. Tasks with deadline that already passed have different color from others.
 */

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

    private var today: Long = Long.MAX_VALUE
    private var maximumDeadline: Long = Long.MIN_VALUE
    private var minimumDeadline: Long = Long.MAX_VALUE

    init {
        today = Calendar.getInstance().timeInMillis
        maximumDeadline = super.listOfTasks.map { it.deadline.toInstant().toEpochMilli() }.maxOrNull() ?: Long.MIN_VALUE
        minimumDeadline = super.listOfTasks.map { it.deadline.toInstant().toEpochMilli() }.minOrNull() ?: Long.MAX_VALUE
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
        minimumDeadline = min(
            super.listOfTasks.map { it.deadline.toInstant().toEpochMilli() }.maxOrNull() ?: Long.MIN_VALUE,
            task.deadline.toInstant().toEpochMilli())
        return super.add(task)
    }

    override suspend fun delete(task: DeadlineTask): Status {
        maximumDeadline = max(
            super.listOfTasks.map { it.deadline.toInstant().toEpochMilli() }.maxOrNull() ?: Long.MIN_VALUE,
            task.deadline.toInstant().toEpochMilli())
        minimumDeadline = min(
            super.listOfTasks.map { it.deadline.toInstant().toEpochMilli() }.maxOrNull() ?: Long.MIN_VALUE,
            task.deadline.toInstant().toEpochMilli())
        return super.delete(task)
    }

    override fun getPriority(id: Int): Double {
        val currentTask = super.listOfTasks[id]
        val dateInt = currentTask.deadline.toInstant().toEpochMilli()
        today = Calendar.getInstance().timeInMillis

        if (maximumDeadline < today) {
            maximumDeadline = today + TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)
        }

        currentTask.evaluatedPriority = sqrt(1 - (dateInt - today)  * 1.0 / (maximumDeadline - today)) * MAXIMUM_PRIORITY
        //evaluates priority: scales between 0 - 100 asymptotically to root function
        //priorities higher than 100 indicate that deadline has passed
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
    private var minimumPriority = Int.MAX_VALUE

    init {
        maximumPriority = super.listOfTasks.map { it.priority }.maxOrNull() ?: Int.MIN_VALUE
        minimumPriority = super.listOfTasks.map { it.priority }.minOrNull() ?: Int.MAX_VALUE
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
        if (status.code == StatusCodes.SUCCESS) {
            maximumPriority = max(
                super.listOfTasks.map { it.priority }.maxOrNull() ?: Int.MIN_VALUE,
                task.priority
            )
            minimumPriority = min(
                super.listOfTasks.map { it.priority }.minOrNull() ?: Int.MAX_VALUE,
                task.priority
            )
        }
        return status
    }

    override suspend fun delete(task: PriorityTask): Status {
        maximumPriority = max(
            super.listOfTasks.map { it.priority }.maxOrNull() ?: Int.MIN_VALUE,
            task.priority)
        minimumPriority = max(
            super.listOfTasks.map { it.priority }.minOrNull() ?: Int.MAX_VALUE,
            task.priority)
        return super.delete(task)
    }

    override fun getPriority(id: Int): Double {
        val currentTask = super.listOfTasks[id]
        val currentPriority = currentTask.priority

        currentTask.evaluatedPriority = sqrt((currentPriority - minimumPriority) * 1.0/(maximumPriority - minimumPriority)) * MAXIMUM_PRIORITY
        //evaluates priority: scales values on range min priority - max priority to 0 - 100 asymptotically to root function
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