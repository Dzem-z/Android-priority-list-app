package com.example.prioritylist.data

import java.time.LocalDateTime
import java.util.Date

/*
TODO(comments)
 */


open class Task(
    open val dateOfCreation: LocalDateTime,
    open var evaluatedPriority: Double = 0.0,
    open var id: Int = 0,
    open val description: String,
    open val name: String
){
    override operator fun equals(task: Any?)
        = (task is Task)
            && dateOfCreation == task.dateOfCreation
            && id == task.id
            && description == task.description
            && name == task.name
}

data class CategoryTask(
    val category: Category,
    override val dateOfCreation: LocalDateTime,
    override var evaluatedPriority: Double = 0.0,
    override var id: Int = 0,
    override val description: String,
    override val name: String
) : Task(
    dateOfCreation = dateOfCreation,
    evaluatedPriority = evaluatedPriority,
    id = id,
    description = description,
    name = name
) {
    override fun equals(task: Any?)
            = (task is CategoryTask)
            && super.equals(task)
            && category == task.category
}

data class DeadlineTask(
    val deadline: LocalDateTime,
    override val dateOfCreation: LocalDateTime,
    override var evaluatedPriority: Double = 0.0,
    override var id: Int = 0,
    override val description: String,
    override val name: String
) : Task(
    dateOfCreation = dateOfCreation,
    evaluatedPriority = evaluatedPriority,
    id = id,
    description = description,
    name = name
){
    override fun equals(task: Any?)
            = (task is DeadlineTask)
            && super.equals(task)
            && deadline == task.deadline
}


data class PriorityTask(
    val priority: Int,
    override val dateOfCreation: LocalDateTime,
    override var evaluatedPriority: Double = 0.0,
    override var id: Int = 0,
    override val description: String,
    override val name: String
) : Task(
    dateOfCreation = dateOfCreation,
    evaluatedPriority = evaluatedPriority,
    id = id,
    description = description,
    name = name
){
    override fun equals(task: Any?)
            = (task is PriorityTask)
            && super.equals(task)
            && priority == task.priority
}


data class DeadlinePriorityTask(
    val priority: Int,
    val deadline: LocalDateTime,
    override val dateOfCreation: LocalDateTime,
    override var evaluatedPriority: Double = 0.0,
    override var id: Int = 0,
    override val description: String,
    override val name: String
) : Task(
    dateOfCreation = dateOfCreation,
    evaluatedPriority = evaluatedPriority,
    id = id,
    description = description,
    name = name
){
    override fun equals(task: Any?)
            = (task is DeadlinePriorityTask)
            && super.equals(task)
            && deadline == task.deadline
            && priority == task.priority
}

data class DeadlineCategoryTask(
    val deadline: LocalDateTime,
    val category: Category,
    override val dateOfCreation: LocalDateTime,
    override var evaluatedPriority: Double = 0.0,
    override var id: Int = 0,
    override val description: String,
    override val name: String
) : Task(
    dateOfCreation = dateOfCreation,
    evaluatedPriority = evaluatedPriority,
    id = id,
    description = description,
    name = name
){
    override fun equals(task: Any?)
            = (task is DeadlineCategoryTask)
            && super.equals(task)
            && deadline == task.deadline
            && category == task.category
}


data class DeadlinePriorityCategoryTask(
    val deadline: LocalDateTime,
    val category: Category,
    val priority: Int,
    override val dateOfCreation: LocalDateTime,
    override var evaluatedPriority: Double = 0.0,
    override var id: Int = 0,
    override val description: String,
    override val name: String
) : Task(
    dateOfCreation = dateOfCreation,
    evaluatedPriority = evaluatedPriority,
    id = id,
    description = description,
    name = name
){
    override fun equals(task: Any?)
            = (task is DeadlinePriorityCategoryTask)
            && super.equals(task)
            && deadline == task.deadline
            && priority == task.priority
            && category == task.category
}