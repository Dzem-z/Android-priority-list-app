package com.example.prioritylist.data.database


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "TasksTable", primaryKeys = ["name", "listID"])
data class TaskEntity(
    val name: String,
    val description: String,
    val dateOfCreation: Date,
    val priority: Int,
    val category: Int,
    val deadline: Date,
    val listID: Int,
    val type: Int
    )

@Entity(tableName = "ListsTable")
data class ListEntity(
    @PrimaryKey
    val listID: Int,
    val name: String,
    val type: Int,
    val currentID: Int
)

/*
@Entity(tableName = "HistoryTasksTable")
data class HistoryTaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val historyTaskID: Int,
    val name: String,
    val listID: Int,
    val dateOfCompletion: Date
)

data class HistoryTaskData(
    @Embedded val task: TaskEntity,
    @Relation(
        parentColumn = ""
    )
    )
 */