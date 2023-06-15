package com.example.prioritylist.data.database

import com.example.prioritylist.data.database.ListEntity
import com.example.prioritylist.data.database.TaskEntity
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    fun loadListByID(listID: Int): Flow<List<TaskEntity>>

    fun loadListCredentials(): Flow<List<ListEntity>>

    suspend fun saveList(list: ListEntity)
}

interface ListRepository {
    suspend fun add(task: TaskEntity)

    suspend fun delete(task: TaskEntity)

    suspend fun changeName(listID: Int, newName: String)
}

interface HistoryRepository {
    //TODO
}