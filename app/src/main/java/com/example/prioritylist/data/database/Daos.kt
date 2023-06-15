package com.example.prioritylist.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add( task: TaskEntity)

    @Delete
    suspend fun delete(task: TaskEntity)

    @Query("UPDATE listsTable SET name = :newName WHERE listID = :listID")
    suspend fun changeName(listID: Int, newName: String)
}

@Dao
interface MainDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveList(list: ListEntity)

    @Query("SELECT * FROM ListsTable")
    fun loadListCredentials(): Flow<List<ListEntity>>

    @Query("SELECT * FROM TasksTable WHERE listID = :listID")
    fun loadListByID(listID: Int): Flow<List<TaskEntity>>

}

@Dao
interface HistoryDao{
    //TODO
}