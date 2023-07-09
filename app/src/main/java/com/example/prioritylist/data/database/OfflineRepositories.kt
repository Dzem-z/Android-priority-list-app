package com.example.prioritylist.data.database

import com.example.prioritylist.data.database.ListRepository
import java.util.Date

/**
 * An implementation of Database repositories interfaces
 */


class OfflineMainRepository(private val mainDao: MainDao): MainRepository {

    override fun loadTaskListByID(listID: Int): List<TaskEntity> = mainDao.loadTaskListByID(listID)

    override fun loadHistoryListByID(listID: Int): List<TaskEntity> = mainDao.loadHistoryListByID(listID)

    override fun loadListCredentials(): List<ListEntity> = mainDao.loadListCredentials()

    override suspend fun saveList(list: ListEntity) = mainDao.saveList(list)

    override suspend fun changeIdOfCurrent(listID: Int, newID: Int){

        if (listID > newID) {
            shift(newID, 1, 100_000)
            mainDao.changeIdOfCurrent(listID + 1, newID)
            shift(listID + 1, -1, 100_000)
        }
        else {
            shift(newID + 1, 1, 100_000)
            mainDao.changeIdOfCurrent(listID, newID + 1)
            shift(listID, -1, 100_000)
        }

    }

    override suspend fun shift(startingID: Int, value: Int, size: Int){
        mainDao.shiftForward(startingID, value, size)
        mainDao.shiftBackward(size)
    }

    override suspend fun deleteList(listID: Int) {
        mainDao.deleteListCredentials(listID)
        mainDao.deleteListTasks(listID)
    }
}

class OfflineListRepository(private val listDao: ListDao): ListRepository {

    override suspend fun add(task: TaskEntity) = listDao.add(task)

    override suspend fun update(task: TaskEntity) = listDao.update(task)

    override suspend fun delete(task: TaskEntity) = listDao.delete(task)

    override suspend fun changeIdOfCurrent(listID: Int, newID: Int) {
        if (listID > newID) {
            shift(newID, 1, 100_000)
            listDao.changeIdOfCurrentList(listID + 1, newID)
            shift(listID + 1, -1, 100_000)
        }
        else {
            shift(newID + 1, 1, 100_000)
            listDao.changeIdOfCurrentList(listID, newID + 1)
            shift(listID, -1, 100_000)
        }
    }

    override suspend fun shift(startingID: Int, value: Int, size: Int){
        listDao.shiftForward(startingID, value, size)
        listDao.shiftBackward(size)
    }

    override suspend fun updateDateOfCompletion(
        name: String, listID: Int, dateOfCompletion: Date)
    = listDao.updateDateOfCompletion(name, listID, dateOfCompletion)

}