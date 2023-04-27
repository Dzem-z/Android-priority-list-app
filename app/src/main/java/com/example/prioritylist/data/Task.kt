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
)

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
)

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
)

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
)

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
)

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
)

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
)