package com.example.prioritylist.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [TaskEntity::class, ListEntity::class, CategoryEntity::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class database : RoomDatabase() {

    abstract fun listDao(): ListDao
    abstract fun mainDao(): MainDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        @Volatile
        private var Instance : database? = null

        fun getDatabase(context: Context): database {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, database::class.java, "priority_list_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}