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
        //this.deleteDatabase("priority_list_database");
        super.onCreate()
        container = AppDataContainer(this)
    }
}