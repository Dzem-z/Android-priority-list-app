package com.example.prioritylist.ui

import androidx.lifecycle.ViewModel
import com.example.prioritylist.data.ModifiableTask
import com.example.prioritylist.data.Status
import com.example.prioritylist.data.Task
import com.example.prioritylist.data.TaskTypes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
/*
TODO(comments)
 */

class StateHolder : ViewModel() {

    var editedTask by mutableStateOf(ModifiableTask())
    private val _currentType: MutableStateFlow<TaskTypes> = MutableStateFlow(TaskTypes.PRIORITY)
    val currentType: StateFlow<TaskTypes> = _currentType.asStateFlow()
    var displayingList: MutableStateFlow<MutableList<out Task>> = MutableStateFlow(mutableListOf<Task>())

    fun updateNameOfEditedTask(newName: String){
        val newTask = editedTask.copy()
        newTask.name = newName
        editedTask = newTask
    }

    fun updateDescriptionOfEditedTask(newDescription: String){
        val newTask = editedTask.copy()
        newTask.description = newDescription
        editedTask = newTask
    }

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