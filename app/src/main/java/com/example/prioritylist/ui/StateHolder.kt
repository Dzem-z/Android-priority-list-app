package com.example.prioritylist.ui

import androidx.lifecycle.ViewModel
import com.example.prioritylist.data.Status
import com.example.prioritylist.data.Task
import com.example.prioritylist.data.TaskTypes

/*
TODO(comments)
 */

class StateHolder(
    private var editedTask: Task? = null,
    var currentType: TaskTypes,
    var displayingList: List<Task>
    ) : ViewModel() {
    fun onDeleteTask(task: Task){
        TODO("Not yet implemented")
    }
    fun onUndo(){
        TODO("Not yet implemented")

    }
    fun nextList(){
        TODO("Not yet implemented")
    }
    fun prevList(){
        TODO("Not yet implemented")
    }
    fun onDone(task: Task) {
        TODO("Not yet implemented")
    }
    fun updateTask(task: Task){
        TODO("Not yet implemented")
    }
    fun addTask(task: Task): Status {
        TODO("Not yet implemented")
    }
}