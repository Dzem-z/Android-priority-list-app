package com.example.prioritylist.data.database


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.util.TableInfo
import com.example.prioritylist.data.backend.TaskTypes
import androidx.compose.ui.graphics.Color
import java.util.Date

/**
 * Entities representing serializable objects, that are stored in a database.
 * [TaskEntity] represents [Task] object and its children
 * [ListEntity] represents [TaskList]
 * [CategoryEntity] represents [Category]
 * */

@Entity(tableName = "TasksTable")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val taskID: Int = 0,
    val name: String,
    val description: String,
    val dateOfCreation: Date,
    val priority: Int?,
    val category: Int?,
    val deadline: Date?,
    val listID: Int,
    val type: TaskTypes,
    val dateOfCompletion: Date?
)

@Entity(tableName = "ListsTable")
data class ListEntity(
    @PrimaryKey
    val listID: Int,
    val name: String,
    val dateOfCreation: Date,
    val type: TaskTypes
)

@Entity(tableName = "CategoriesTable")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val categoryID: Int = 0,
    val name: String,
    val color: Int,
    val description: String,
    val priority: Int
)
