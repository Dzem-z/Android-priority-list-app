package com.example.prioritylist.domain

import com.example.prioritylist.data.HistoryTask
import com.example.prioritylist.data.Status
import com.example.prioritylist.data.Task
import com.example.prioritylist.data.TaskTypes
import java.util.Date

/*
TODO(comments)
 */

class DataManager {
    fun prevListUseCase(): TaskTypes {
        TODO("Not yet implemented")
    }
    fun addTaskUseCase(task: Task): Status {
        TODO("Not yet implemented")
    }
    fun editTaskUseCase(task: Task): Status {
        TODO("Not yet implemented")
    }
    fun deleteTaskUseCase(task: Task): Status {
        TODO("Not yet implemented")
    }
    fun moveToHistoryUseCase(task: Task): Status {
        TODO("Not yet implemented")
    }
    fun getListUseCase(): MutableList<HistoryTask<*>> {
        TODO("Not yet implemented")
    }
    fun deleteFromHistoryUseCase(name: String): Status {
        TODO("Not yet implemented")
    }
    fun deleteUntilUseCase(date: Date): Status {
        TODO("Not yet implemented")
    }
    fun changeNameUseCase(name: String) {
        TODO("Not yet implemented")
    }
    fun undoUseCase() {
        TODO("Not yet implemented")
    }
    fun changeIDUseCase(newId: Int) {
        TODO("Not yet implemented")
    }
    fun nextListUseCase(): TaskTypes {
        TODO("Not yet implemented")
    }
    fun addListUseCase(id: Int, name: String, type: TaskTypes) {
        TODO("Not yet implemented")
    }
    fun deleteCurrentListUseCase(): Status {
        TODO("Not yet implemented")
    }

}