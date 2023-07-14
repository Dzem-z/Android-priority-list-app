package com.example.prioritylist.data.backend

import com.example.prioritylist.data.backend.CategoryTask
import com.example.prioritylist.data.backend.CategoryTaskList
import com.example.prioritylist.data.backend.DeadlineCategoryTask
import com.example.prioritylist.data.backend.DeadlineCategoryTaskList
import com.example.prioritylist.data.backend.DeadlinePriorityCategoryTask
import com.example.prioritylist.data.backend.DeadlinePriorityCategoryTaskList
import com.example.prioritylist.data.backend.DeadlinePriorityTask
import com.example.prioritylist.data.backend.DeadlinePriorityTaskList
import com.example.prioritylist.data.backend.DeadlineTask
import com.example.prioritylist.data.backend.DeadlineTaskList
import com.example.prioritylist.data.backend.PriorityTask
import com.example.prioritylist.data.backend.PriorityTaskList
import com.example.prioritylist.data.backend.Task
import com.example.prioritylist.data.backend.TaskList
import com.example.prioritylist.data.database.MainRepository
import com.example.prioritylist.data.database.ListRepository
import com.example.prioritylist.data.database.TaskEntity
import java.util.Date
import kotlin.reflect.KClass

/*
TODO(comments)
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
            listOfHistoryEntities: List<TaskEntity>
        ): TaskList<out Task> {
            return CategoryTaskList(name, id, dateOfCreation, listRepository)
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
            listOfHistoryEntities: List<TaskEntity>
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
            listOfHistoryEntities: List<TaskEntity>
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
            listOfHistoryEntities: List<TaskEntity>
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
            listOfHistoryEntities: List<TaskEntity>
        ): TaskList<out Task> {
            val listOfTasks = listOfEntities.map {
                DeadlinePriorityTask(deadline = it.deadline!!, priority = it.priority!!, dateOfCreation = it.dateOfCreation, description = it.description, name = it.name)
            }.toMutableList()
            return DeadlinePriorityTaskList(name, id, dateOfCreation, listRepository, listOfTasks)
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
            listOfHistoryEntities: List<TaskEntity>
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

    abstract fun hasPriority(): Boolean
    abstract fun hasCategory(): Boolean
    abstract fun hasDeadline(): Boolean

    abstract fun create(
        name: String,
        id: Int,
        dateOfCreation: Date,
        listRepository: ListRepository,
        listOfEntities: List<TaskEntity> = listOf(),
        listOfHistoryEntities: List<TaskEntity> = listOf()
    ): TaskList<out Task>

    abstract fun toTask(editedTask: ModifiableTask): Task
}

val listOfTypes = arrayOf(
    TaskTypes.PRIORITY, TaskTypes.DEADLINE
)
