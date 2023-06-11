package com.example.prioritylist.data.backend

import com.example.prioritylist.data.backend.Category
import java.util.Calendar
import java.util.Date

/*
TODO(comments)
 */


open class Task(
    open val dateOfCreation: Date,
    open var evaluatedPriority: Double = 0.0,
    open var id: Int = 0,
    open val description: String,
    open val name: String
){
    override operator fun equals(task: Any?)
        = (task is Task)
            && dateOfCreation == task.dateOfCreation
            && description == task.description
            && name == task.name
}

data class CategoryTask(
    val category: Category,
    override val dateOfCreation: Date,
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
    val deadline: Date,
    override val dateOfCreation: Date,
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
    override val dateOfCreation: Date,
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
    val deadline: Date,
    override val dateOfCreation: Date,
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
    val deadline: Date,
    val category: Category,
    override val dateOfCreation: Date,
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
    val deadline: Date,
    val category: Category,
    val priority: Int,
    override val dateOfCreation: Date,
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


data class ModifiableTask(
    var deadline: Date = Calendar.getInstance().time,
    var category: Category? = null,
    var priority: Int = 0,
    var dateOfCreation: Date = Calendar.getInstance().time,
    var evaluatedPriority: Double = 0.0,
    var id: Int = 0,
    var description: String = "",
    var name: String = ""
)