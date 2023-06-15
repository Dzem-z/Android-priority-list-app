package com.example.prioritylist.data.database

import com.example.prioritylist.data.database.ListEntity
import com.example.prioritylist.data.database.TaskEntity
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    fun loadListByID(listID: Int): List<TaskEntity>

    fun loadListCredentials(): List<ListEntity>

    suspend fun saveList(list: ListEntity)

    suspend fun changeIdOfCurrent(listID: Int, newID: Int)

    suspend fun shift(startingID: Int, value: Int, size: Int)

    suspend fun deleteList(listID: Int)
}

interface ListRepository {
    suspend fun add(task: TaskEntity)

    suspend fun delete(task: TaskEntity)

    suspend fun update(task: TaskEntity)
}

interface HistoryRepository {
    //TODO
}