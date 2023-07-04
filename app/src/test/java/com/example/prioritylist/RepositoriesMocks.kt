package com.example.prioritylist

import com.example.prioritylist.data.database.ListEntity
import com.example.prioritylist.data.database.MainRepository
import com.example.prioritylist.data.database.TaskEntity
import com.example.prioritylist.data.database.ListRepository
import java.util.Date

class MainRepositoryMock : MainRepository {
    override suspend fun changeIdOfCurrent(listID: Int, newID: Int) {

    }

    override suspend fun deleteList(listID: Int) {

    }

    override fun loadListByID(listID: Int): List<TaskEntity> {
        return listOf()
    }

    override fun loadListCredentials(): List<ListEntity> {
        return listOf()
    }

    override suspend fun saveList(list: ListEntity) {

    }

    override suspend fun shift(startingID: Int, value: Int, size: Int) {

    }

}

class ListRepositoryMock : ListRepository {
    override suspend fun add(task: TaskEntity){}

    override suspend fun delete(task: TaskEntity){}


    override suspend fun update(task: TaskEntity){}

    override suspend fun updateDateOfCompletion(name: String, listID: Int, dateOfCompletion: Date){}
}

