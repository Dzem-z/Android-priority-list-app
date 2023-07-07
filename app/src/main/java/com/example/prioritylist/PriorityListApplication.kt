package com.example.prioritylist

import android.app.Application
import com.example.prioritylist.data.AppContainer
import com.example.prioritylist.data.AppDataContainer

class PriorityListApplication : Application() {
    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}