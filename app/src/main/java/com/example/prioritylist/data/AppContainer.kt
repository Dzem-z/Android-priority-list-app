package com.example.prioritylist.data

import android.content.Context
import com.example.prioritylist.data.backend.MainPage
import com.example.prioritylist.data.database.CategoryRepository
import com.example.prioritylist.data.database.ListRepository
import com.example.prioritylist.data.database.MainRepository
import com.example.prioritylist.data.database.OfflineCategoryRepository
import com.example.prioritylist.data.database.OfflineListRepository
import com.example.prioritylist.data.database.OfflineMainRepository
import com.example.prioritylist.data.database.database

/**
 * App container for Dependency injection.
 */

interface AppContainer {
    val mainRepository: MainRepository
    val listRepository: ListRepository
    val categoryRepository: CategoryRepository
    val mainPageRepository: MainPage
}

/**
 * [AppContainer] implementation that provides instance of [OfflineMainRepository]
 * and [OfflineListRepository]
 */

class AppDataContainer(private val context: Context): AppContainer {
    /**
     * Implementation for [MainRepository]
     */
    override val mainRepository: MainRepository by lazy {
        OfflineMainRepository(database.getDatabase(context).mainDao())
    }
    /**
     * Implementation for [ListRepository]
     */
    override val listRepository: ListRepository by lazy {
        OfflineListRepository(database.getDatabase(context).listDao())
    }
    /**
     * Implementation for [CategoryRepository]
     */
    override val categoryRepository: CategoryRepository by lazy {
        OfflineCategoryRepository(database.getDatabase(context).categoryDao())
    }

    override val mainPageRepository : MainPage = MainPage(listRepository, mainRepository, categoryRepository)
}