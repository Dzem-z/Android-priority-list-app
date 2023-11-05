package com.example.prioritylist.data.backend

import androidx.compose.ui.graphics.Color
import com.example.prioritylist.data.database.CategoryRepository
import com.example.prioritylist.data.database.ListRepository
import com.example.prioritylist.data.database.TaskEntity
import java.util.Date
import kotlin.reflect.KClass

/**
 * [TaskTypes] is an enum that holds an information about specified type such as presence of certain attributes.
 * It is also capable of creating list holding specified type and can convert from [ModifiableTask]
 *
 * @param taskType is [KClass] of the specified task type
 * @param listType is [KClass] of the specified list type
 */

enum class TaskTypes(val taskType: KClass<out Task>, val listType: KClass<out TaskList<*>>) {
    CATEGORY(CategoryTask::class, CategoryTaskList::class){
        override fun hasPriority() = false
        override fun hasCategory() = true
        override fun hasDeadline() = false

        override fun create(
            name: String,
            id: Int,
            dateOfCreation: Date,
            listRepository: ListRepository,
            listOfEntities: List<TaskEntity>,
            listOfHistoryEntities: List<TaskEntity>,
            categoryRepository: CategoryRepository?
        ): TaskList<out Task> {
            val listOfTasks = listOfEntities.mapIndexed { index, it ->
                val categoryEntity = categoryRepository!!.getCategoryByID(it.category!!)
                val category = Category(name = categoryEntity.name, color = Color(categoryEntity.color), id = categoryEntity.categoryID, description = categoryEntity.description, priority = categoryEntity.priority )
                CategoryTask(category = category, dateOfCreation = it.dateOfCreation, description = it.description, name = it.name, id = index)
            }.toMutableList()

            val listOfHistoryTasks = listOfHistoryEntities.mapIndexed { index, it ->
                val categoryEntity = categoryRepository!!.getCategoryByID(it.category!!)
                val category = Category(name = categoryEntity.name, color = Color(categoryEntity.color), id = categoryEntity.categoryID, description = categoryEntity.description, priority = categoryEntity.priority )
                HistoryTask<CategoryTask>(CategoryTask(category = category, dateOfCreation = it.dateOfCreation, description = it.description, name = it.name, id = index), it.dateOfCompletion!!)
            }.toMutableList()

            return CategoryTaskList(name, id, dateOfCreation, listRepository, listOfTasks, listOfHistoryTasks)
        }

        override fun toTask(editedTask: ModifiableTask): Task {
            return CategoryTask(category = editedTask.category!!, dateOfCreation = editedTask.dateOfCreation, description = editedTask.description, name = editedTask.name)
        }
                                                          },
    PRIORITY(PriorityTask::class, PriorityTaskList::class){
        override fun hasPriority() = true
        override fun hasCategory() = false
        override fun hasDeadline() = false

        override fun create(
            name: String,
            id: Int,
            dateOfCreation: Date,
            listRepository: ListRepository,
            listOfEntities: List<TaskEntity>,
            listOfHistoryEntities: List<TaskEntity>,
            categoryRepository: CategoryRepository?
        ): TaskList<out Task> {
            val listOfTasks = listOfEntities.mapIndexed { index, it ->
                PriorityTask(priority = it.priority!!, dateOfCreation = it.dateOfCreation, description = it.description, name = it.name, id = index)
            }.toMutableList()
            val listOfHistoryTasks = listOfHistoryEntities.mapIndexed { index, it ->
                HistoryTask<PriorityTask>(PriorityTask(priority = it.priority!!, dateOfCreation = it.dateOfCreation, description = it.description, name = it.name, id = index), it.dateOfCompletion!!)
            }.toMutableList()
            return PriorityTaskList(name, id, dateOfCreation, listRepository, listOfTasks, listOfHistoryTasks)
        }

        override fun toTask(editedTask: ModifiableTask): Task {
            return PriorityTask(priority = editedTask.priority!!, dateOfCreation = editedTask.dateOfCreation, description = editedTask.description, name = editedTask.name)
        }
                                                          },
    DEADLINE_PRIORITY_CATEGORY(DeadlinePriorityCategoryTask::class, DeadlinePriorityCategoryTaskList::class) {
        override fun hasPriority() = true
        override fun hasCategory() = true
        override fun hasDeadline() = true

        override fun create(
            name: String,
            id: Int,
            dateOfCreation: Date,
            listRepository: ListRepository,
            listOfEntities: List<TaskEntity>,
            listOfHistoryEntities: List<TaskEntity>,
            categoryRepository: CategoryRepository?
        ): TaskList<out Task> {
            return DeadlinePriorityCategoryTaskList(name, id, dateOfCreation, listRepository)
        }

        override fun toTask(editedTask: ModifiableTask): Task {
            return DeadlinePriorityTask(deadline = editedTask.deadline!!, priority = editedTask.priority!!, dateOfCreation = editedTask.dateOfCreation, description = editedTask.description, name = editedTask.name)
        }
                                                    },
    DEADLINE_CATEGORY(DeadlineCategoryTask::class, DeadlineCategoryTaskList::class){
        override fun hasPriority() = false
        override fun hasCategory() = true
        override fun hasDeadline() = true

        override fun create(
            name: String,
            id: Int,
            dateOfCreation: Date,
            listRepository: ListRepository,
            listOfEntities: List<TaskEntity>,
            listOfHistoryEntities: List<TaskEntity>,
            categoryRepository: CategoryRepository?
        ): TaskList<out Task> {
            return DeadlineCategoryTaskList(name, id, dateOfCreation, listRepository)
        }

        override fun toTask(editedTask: ModifiableTask): Task {
            return DeadlineCategoryTask(deadline = editedTask.deadline!!, category = editedTask.category!!, dateOfCreation = editedTask.dateOfCreation, description = editedTask.description, name = editedTask.name)
        }
                                                                                    },
    DEADLINE_PRIORITY(DeadlinePriorityTask::class, DeadlinePriorityTaskList::class){
        override fun hasPriority() = true
        override fun hasCategory() = false
        override fun hasDeadline() = true

        override fun create(
            name: String,
            id: Int,
            dateOfCreation: Date,
            listRepository: ListRepository,
            listOfEntities: List<TaskEntity>,
            listOfHistoryEntities: List<TaskEntity>,
            categoryRepository: CategoryRepository?
        ): TaskList<out Task> {
            val listOfTasks = listOfEntities.mapIndexed { index, it ->
                DeadlinePriorityTask(deadline = it.deadline!!, priority = it.priority!!, dateOfCreation = it.dateOfCreation, description = it.description, name = it.name, id = index)
            }.toMutableList()
            val listOfHistoryTasks = listOfHistoryEntities.mapIndexed { index, it ->
                HistoryTask<DeadlinePriorityTask>(DeadlinePriorityTask(priority = it.priority!!, deadline = it.deadline!!, dateOfCreation = it.dateOfCreation, description = it.description, name = it.name, id = index), it.dateOfCompletion!!)
            }.toMutableList()
            return DeadlinePriorityTaskList(name, id, dateOfCreation, listRepository, listOfTasks, listOfHistoryTasks)
        }

        override fun toTask(editedTask: ModifiableTask): Task {
            return DeadlinePriorityTask(deadline = editedTask.deadline!!, priority = editedTask.priority!!, dateOfCreation = editedTask.dateOfCreation, description = editedTask.description, name = editedTask.name)
        }
                                                                                   },
    DEADLINE(DeadlineTask::class, DeadlineTaskList::class){
        override fun hasPriority() = false
        override fun hasCategory() = false
        override fun hasDeadline() = true

        override fun create(
            name: String,
            id: Int,
            dateOfCreation: Date,
            listRepository: ListRepository,
            listOfEntities: List<TaskEntity>,
            listOfHistoryEntities: List<TaskEntity>,
            categoryRepository: CategoryRepository?
        ): TaskList<out Task> {
            val listOfTasks = listOfEntities.mapIndexed {index, it ->
                DeadlineTask(deadline = it.deadline!!, dateOfCreation = it.dateOfCreation, description = it.description, name = it.name, id = index)
            }.toMutableList()
            val listOfHistoryTasks = listOfHistoryEntities.mapIndexed { index, it ->
                HistoryTask<DeadlineTask>(DeadlineTask(deadline = it.deadline!!, dateOfCreation = it.dateOfCreation, description = it.description, name = it.name, id = index), it.dateOfCompletion!!)
            }.toMutableList()
            return DeadlineTaskList(name, id, dateOfCreation, listRepository, listOfTasks, listOfHistoryTasks)
        }

        override fun toTask(editedTask: ModifiableTask): Task {
            return DeadlineTask(deadline = editedTask.deadline, dateOfCreation = editedTask.dateOfCreation, description = editedTask.description, name = editedTask.name)
        }
                                                            };


    abstract fun hasPriority(): Boolean //returns true if list of that type has priority
    abstract fun hasCategory(): Boolean //returns true if list of that type has category
    abstract fun hasDeadline(): Boolean //returns true if list of that type has deadline

    /**
     * create is an factory method that produces [TaskList]
     *
     * @param name is a name of list
     * @param id is a id of list
     * @param dateOfCreation is a date of creation of a list
     * @param listRepository is a reference to the listRepository
     * @param listOfEntities is a list of [TaskEntity] of this list
     * @param listOfHistoryEntities is a list of [TaskEntity] of history list associated with that list
     */

    abstract fun create(
        name: String,
        id: Int,
        dateOfCreation: Date,
        listRepository: ListRepository,
        listOfEntities: List<TaskEntity> = listOf(),
        listOfHistoryEntities: List<TaskEntity> = listOf(),
        categoryRepository: CategoryRepository? = null
    ): TaskList<out Task>

    /**
     * converts [ModifiableTask] to [Task] of specified type
     *
     * @param editedTask task to be converted from
     */

    abstract fun toTask(editedTask: ModifiableTask): Task
}

val listOfTypes = arrayOf(  //list of types used by TaskList
    TaskTypes.PRIORITY, TaskTypes.DEADLINE, TaskTypes.DEADLINE_PRIORITY
)
