package com.example.prioritylist.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.util.Date

/**
 * Dao used by [OfflineListRepository]
 */
@Dao
interface ListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add( task: TaskEntity)

    @Query("DELETE FROM TasksTable WHERE name = :taskName AND listID = :listID")
    suspend fun delete(taskName: String, listID: Int)

    @Update
    suspend fun update(newTask: TaskEntity)

    @Query("UPDATE TasksTable SET listID = listID + :size + :value WHERE listID >= :startingID;")
    suspend fun shiftForward(startingID: Int, value: Int, size: Int)

    @Query("UPDATE TasksTable SET listID = listID - :size WHERE listID >= :size")
    suspend fun shiftBackward(size: Int)

    @Query("UPDATE TasksTable SET listID = :newCurrent WHERE listID = :listID")
    suspend fun changeIdOfCurrentList(listID: Int, newCurrent: Int)

    @Query("UPDATE TasksTable SET dateOfCompletion = :dateOfCompletion WHERE listID = :listID AND name = :name")
    suspend fun updateDateOfCompletion(name: String, listID: Int, dateOfCompletion: Date)
}

/**
 * Dao used by [OfflineMainRepository]
 */
@Dao
interface MainDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveList(list: ListEntity)

    @Query("DELETE FROM ListsTable WHERE listID = :listID")
    suspend fun deleteListCredentials(listID: Int)

    @Query("DELETE FROM TasksTable WHERE listID = :listID")
    suspend fun deleteListTasks(listID: Int)

    @Query("UPDATE ListsTable SET listID = :newCurrent WHERE listID = :listID")
    suspend fun changeIdOfCurrent(listID: Int, newCurrent: Int)

    @Query("UPDATE ListsTable SET listID = listID + :size + :value WHERE listID >= :startingID;")
    suspend fun shiftForward(startingID: Int, value: Int, size: Int)

    @Query("UPDATE ListsTable SET listID = listID - :size WHERE listID >= :size")
    suspend fun shiftBackward(size: Int)

    @Query("SELECT * FROM ListsTable")
    fun loadListCredentials(): List<ListEntity>

    @Query("SELECT * FROM TasksTable WHERE listID = :listID AND dateOfCompletion IS NULL")
    fun loadTaskListByID(listID: Int): List<TaskEntity>

    @Query("SELECT * FROM TasksTable WHERE listID = :listID AND dateOfCompletion IS NOT NULL")
    fun loadHistoryListByID(listID: Int): List<TaskEntity>



}