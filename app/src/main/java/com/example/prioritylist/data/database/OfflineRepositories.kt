package com.example.prioritylist.data.database

import com.example.prioritylist.data.database.ListRepository
import com.example.prioritylist.data.database.MainRepository
import com.example.prioritylist.data.database.ListDao
import com.example.prioritylist.data.database.ListEntity
import com.example.prioritylist.data.database.MainDao
import com.example.prioritylist.data.database.TaskEntity
import kotlinx.coroutines.flow.Flow

class OfflineMainRepository(private val mainDao: MainDao): MainRepository {

    override fun loadListByID(listID: Int): Flow<List<TaskEntity>> = mainDao.loadListByID(listID)

    override fun loadListCredentials(): Flow<List<ListEntity>> = mainDao.loadListCredentials()

    override suspend fun saveCurrent(listID: Int, newCurrent: Int) = mainDao.saveCurrent(listID, newCurrent)

    override suspend fun saveList(list: ListEntity) = mainDao.saveList(list)
}

class OfflineListRepository(private val listDao: ListDao): ListRepository {

    override suspend fun add(task: TaskEntity) = listDao.add(task)

    override suspend fun changeName(listID: Int, newName: String) = listDao.changeName(listID, newName)

    override suspend fun delete(task: TaskEntity) = listDao.delete(task)
}