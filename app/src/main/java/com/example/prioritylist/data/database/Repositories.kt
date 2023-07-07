package com.example.prioritylist.data.database


import java.util.Date

interface MainRepository {

    //loads list of tasks
    fun loadListByID(listID: Int): List<TaskEntity>

    //loads list of all lists saved in database
    fun loadListCredentials(): List<ListEntity>

    suspend fun saveList(list: ListEntity)

    //changes id of list from listID to newID
    suspend fun changeIdOfCurrent(listID: Int, newID: Int)

    //shifts all records with id := id + value when id >= startingID
    suspend fun shift(startingID: Int, value: Int, size: Int)

    suspend fun deleteList(listID: Int)
}

interface ListRepository {
    suspend fun add(task: TaskEntity)

    suspend fun delete(task: TaskEntity)

    suspend fun update(task: TaskEntity)

    //changes id of list from listID to newID
    suspend fun changeIdOfCurrent(listID: Int, newID: Int)

    //shifts all records with id := id + value when id >= startingID
    suspend fun shift(startingID: Int, value: Int, size: Int)

    suspend fun updateDateOfCompletion(name: String, listID: Int, dateOfCompletion: Date)
}