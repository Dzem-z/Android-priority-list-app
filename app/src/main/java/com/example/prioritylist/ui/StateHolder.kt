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
import com.example.prioritylist.data.Category
import com.example.prioritylist.data.CategoryTask
import com.example.prioritylist.data.CategoryTaskList
import com.example.prioritylist.data.DeadlineCategoryTask
import com.example.prioritylist.data.DeadlineCategoryTaskList
import com.example.prioritylist.data.DeadlinePriorityCategoryTask
import com.example.prioritylist.data.DeadlinePriorityCategoryTaskList
import com.example.prioritylist.data.DeadlinePriorityTask
import com.example.prioritylist.data.DeadlineTask
import com.example.prioritylist.data.PriorityTask
import com.example.prioritylist.data.StatusCodes
import com.example.prioritylist.domain.DataManager
import java.time.LocalDateTime

/*
TODO(comments)
 */

class StateHolder : ViewModel() {

    var editedTask by mutableStateOf(ModifiableTask())
    private val _currentType: MutableStateFlow<TaskTypes> = MutableStateFlow(TaskTypes.PRIORITY)
    val currentType: StateFlow<TaskTypes> = _currentType.asStateFlow()
    var displayingList by mutableStateOf<MutableList<out Task>>(mutableListOf<Task>())
    private val dataManager = DataManager()
    init{
        displayingList = dataManager.getListUseCase()
    }

    fun getType(): TaskTypes{
        return currentType.value
    }

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

    fun updatePriorityOfEditedTask(newPriority: String){
        val newTask = editedTask.copy()
        newTask.priority = newPriority.toInt()
        editedTask = newTask
    }

    fun onAddTask(){

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

    fun onDelete(task: Task) {
        dataManager.deleteTaskUseCase(task)
        displayingList = dataManager.getListUseCase()
    }

    fun onDone(task: Task) {
        dataManager.moveToHistoryUseCase(task)
        displayingList = dataManager.getListUseCase()
    }
    fun updateTask(task: Task){
        TODO("Not yet implemented")
    }
    fun addTask(): Status {

        val status = when(currentType.value){
            TaskTypes.PRIORITY -> dataManager.addTaskUseCase(PriorityTask(name = editedTask.name, description = editedTask.description, dateOfCreation = editedTask.dateOfCreation, priority = editedTask.priority!!))
            TaskTypes.DEADLINE -> dataManager.addTaskUseCase(DeadlineTask(name = editedTask.name, description = editedTask.description, dateOfCreation = editedTask.dateOfCreation, deadline = editedTask.deadline!!))
            TaskTypes.CATEGORY -> dataManager.addTaskUseCase(CategoryTask(name = editedTask.name, description = editedTask.description, dateOfCreation = editedTask.dateOfCreation, category = editedTask.category!!))
            TaskTypes.DEADLINE_CATEGORY -> dataManager.addTaskUseCase(DeadlineCategoryTask(name = editedTask.name, description = editedTask.description, dateOfCreation = editedTask.dateOfCreation, deadline = editedTask.deadline!!, category = editedTask.category!!))
            TaskTypes.DEADLINE_PRIORITY -> dataManager.addTaskUseCase(DeadlinePriorityTask(name = editedTask.name, description =  editedTask.description, dateOfCreation = editedTask.dateOfCreation, deadline = editedTask.deadline!!, priority = editedTask.priority!!))
            TaskTypes.DEADLINE_PRIORITY_CATEGORY -> dataManager.addTaskUseCase(DeadlinePriorityCategoryTask(name  = editedTask.name, description = editedTask.description, dateOfCreation = editedTask.dateOfCreation, deadline = editedTask.deadline!!, priority = editedTask.priority!!, category = editedTask.category!!))
        }

        displayingList = dataManager.getListUseCase()
        return status
    }
}