package com.example.prioritylist.ui

import androidx.annotation.VisibleForTesting
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
import androidx.compose.runtime.remember
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
import com.example.prioritylist.data.HistoryTask
import com.example.prioritylist.data.PriorityTask
import com.example.prioritylist.data.StatusCodes
import com.example.prioritylist.domain.DataManager
import java.util.Calendar
import java.util.Date

/*
TODO(comments)
 */

class StateHolder : ViewModel() {

    var editedTask by mutableStateOf(ModifiableTask())

    var firstType by mutableStateOf<TaskTypes>(TaskTypes.PRIORITY)
    var secondType by mutableStateOf<TaskTypes>(TaskTypes.PRIORITY)

    var firstList by mutableStateOf<MutableList<out Task>>(mutableListOf<Task>())
    var secondList by mutableStateOf<MutableList<out Task>>(mutableListOf<Task>())

    var firstHistoryList by mutableStateOf<MutableList<out HistoryTask<out Task>>>(mutableListOf<HistoryTask<Task>>())
    var secondHistoryList by mutableStateOf<MutableList<out HistoryTask<out Task>>>(mutableListOf<HistoryTask<Task>>())

    var isPrevList by mutableStateOf(false)
    var isNextList by mutableStateOf(false)

    var currentListIndex = MutableStateFlow(0)
    var index = currentListIndex.asStateFlow()
        private set

    var visible by mutableStateOf(true)

    var currentListName by mutableStateOf("")

    var newListName by mutableStateOf("")
    var selectedTypeText by mutableStateOf("priority-based tasks")
    var selectedType by mutableStateOf<TaskTypes>(TaskTypes.PRIORITY)

    private val dataManager = DataManager()
    private var badName by mutableStateOf(false)


    var duplicatedName by mutableStateOf(false)
    var emptyName by mutableStateOf(false)

    var taskBottomSheetExpanded by mutableStateOf(true)



    private fun incrementIndex() {
        currentListIndex.value = (currentListIndex.value + 1) % 2
        index = currentListIndex.asStateFlow()
        visible = !visible
    }

    fun setDuplicatedTaskError() {
        duplicatedName = true
        badName = true
    }

    fun setEmptyNameError() {
        badName = true
        emptyName = true
    }

    fun clearNameErrorFlags() {
        badName = false
        emptyName = false
        duplicatedName = false
    }

    fun isDuplicatedTask(): Boolean {
        return badName
    }

    fun isEmpty(): Boolean {
        return dataManager.isEmptyUseCase()
    }

    fun isAlmostEmpty(): Boolean {
        return !isNextListPresent() && !isPrevListPresent()
    }

    init{
        updateList()
    }

    @VisibleForTesting
    internal fun setList(list: MutableList<out Task>) {
        if (currentListIndex.value.equals(0)){
            firstList = list
        } else {
            secondList = list
        }
    }

    internal fun setHistoryList(list: MutableList<out HistoryTask<out Task>>){
        if(currentListIndex.value.equals(0)){
            firstHistoryList = list
        } else {
            secondHistoryList = list
        }
    }

    fun updateList() {
        setList(dataManager.getListUseCase())
        setHistoryList(dataManager.getHistoryListUseCase())
        isPrevList = isPrevListPresent()
        isNextList = isNextListPresent()
        currentListName = dataManager.getNameUseCase()
    }

    fun getList(): MutableList<out Task> {
        return if (currentListIndex.value.equals(0))
            firstList
        else
            secondList
    }

    fun getHistoryList(): MutableList<out HistoryTask<out Task>> {
        return dataManager.getHistoryListUseCase()
    }

    fun getCurrentType(): TaskTypes{
        return if (currentListIndex.value.equals(0))
            firstType
        else
            secondType
    }

    fun setCurrentType(type: TaskTypes) {
        if (currentListIndex.value.equals(0))
            firstType = type
        else
            secondType = type
    }

    fun getName(): String{
        currentListName = dataManager.getNameUseCase()
        return currentListName
    }

    fun getDateOfCreation(): Date{
        return dataManager.getDateOfCreationUseCase()
    }

    fun setName(): Status{
        return dataManager.changeNameUseCase(currentListName)
    }

    fun resetEditedTask() {
        editedTask = ModifiableTask()
    }


    fun updateNameOfEditedTask(newName: String) {
        val newTask = editedTask.copy()
        newTask.name = newName
        clearNameErrorFlags()
        editedTask = newTask
    }

    fun updateDescriptionOfEditedTask(newDescription: String) {
        val newTask = editedTask.copy()
        newTask.description = newDescription
        editedTask = newTask
    }

    fun updatePriorityOfEditedTask(newPriority: String) {
        val newTask = editedTask.copy()
        newTask.priority = if(newPriority.isEmpty()) 0 else newPriority.toInt()
        editedTask = newTask
    }

    fun onAddTask(){
        resetEditedTask()
    }

    fun onEditTask(){
    }

    fun onDeleteTask(): Status{
        val status = when(getCurrentType()){
            TaskTypes.PRIORITY -> dataManager.deleteTaskUseCase(PriorityTask(name = editedTask.name, description = editedTask.description, dateOfCreation = editedTask.dateOfCreation, priority = editedTask.priority))
            TaskTypes.DEADLINE -> dataManager.deleteTaskUseCase(DeadlineTask(name = editedTask.name, description = editedTask.description, dateOfCreation = editedTask.dateOfCreation, deadline = editedTask.deadline))
            TaskTypes.CATEGORY -> dataManager.deleteTaskUseCase(CategoryTask(name = editedTask.name, description = editedTask.description, dateOfCreation = editedTask.dateOfCreation, category = editedTask.category!!))
            TaskTypes.DEADLINE_CATEGORY -> dataManager.deleteTaskUseCase(DeadlineCategoryTask(name = editedTask.name, description = editedTask.description, dateOfCreation = editedTask.dateOfCreation, deadline = editedTask.deadline, category = editedTask.category!!))
            TaskTypes.DEADLINE_PRIORITY -> dataManager.deleteTaskUseCase(DeadlinePriorityTask(name = editedTask.name, description =  editedTask.description, dateOfCreation = editedTask.dateOfCreation, deadline = editedTask.deadline, priority = editedTask.priority))
            TaskTypes.DEADLINE_PRIORITY_CATEGORY -> dataManager.deleteTaskUseCase(DeadlinePriorityCategoryTask(name  = editedTask.name, description = editedTask.description, dateOfCreation = editedTask.dateOfCreation, deadline = editedTask.deadline, priority = editedTask.priority, category = editedTask.category!!))
        }

        updateList()
        return status
    }
    fun onUndo(){
        TODO("Not yet implemented")

    }
    fun nextList(){
        val returnedType = dataManager.nextListUseCase()
        if(returnedType == null){
            throw Exception()
        } else {
            incrementIndex()
            setCurrentType(returnedType)
            updateList()
        }
    }


    fun prevList(){
        val returnedType = dataManager.prevListUseCase()
        if(returnedType == null){
            throw Exception()
        } else {
            incrementIndex()
            setCurrentType(returnedType)
            updateList()
        }
    }

    fun isNextListPresent(): Boolean{
        return if(dataManager.nextListUseCase() != null){
            dataManager.prevListUseCase()
            true
        } else {
            false
        }
    }

    fun isPrevListPresent(): Boolean{
        return if(dataManager.prevListUseCase() != null){
            dataManager.nextListUseCase()
            true
        } else {
            false
        }
    }

    fun addList() {
        dataManager.addListUseCase(
            dataManager.getIDUseCase() + 1,
            newListName,
            selectedType,
            Calendar.getInstance().time
        )

        incrementIndex()
        updateList()
        setCurrentType(selectedType)
        resetAddListParameters()
    }

    fun resetAddListParameters() {
        selectedType = TaskTypes.PRIORITY
        newListName = ""
        selectedTypeText = "priority-based tasks"
    }

    fun removeList() {
        val returnedType = dataManager.deleteCurrentListUseCase()
        if( returnedType != null ){
            incrementIndex()
            setCurrentType(returnedType)
            updateList()
        } else {
            /*TODO(empty screen)*/
        }
    }

    fun swapWithLeft() {
        dataManager.changeIDUseCase(dataManager.getIDUseCase() - 1)
        updateList()
    }

    fun swapWithRight() {
        dataManager.changeIDUseCase(dataManager.getIDUseCase() + 1)
        updateList()
    }

    fun onDelete(task: Task) {

    }

    fun onDone(task: Task) {
        //dataManager.moveToHistoryUseCase(task)
        updateList()
    }
    fun updateTask(task: Task){
        TODO("Not yet implemented")
    }
    fun addTask(): Status {

        val status = when(getCurrentType()){
            TaskTypes.PRIORITY -> dataManager.addTaskUseCase(PriorityTask(name = editedTask.name, description = editedTask.description, dateOfCreation = editedTask.dateOfCreation, priority = editedTask.priority))
            TaskTypes.DEADLINE -> dataManager.addTaskUseCase(DeadlineTask(name = editedTask.name, description = editedTask.description, dateOfCreation = editedTask.dateOfCreation, deadline = editedTask.deadline))
            TaskTypes.CATEGORY -> dataManager.addTaskUseCase(CategoryTask(name = editedTask.name, description = editedTask.description, dateOfCreation = editedTask.dateOfCreation, category = editedTask.category!!))
            TaskTypes.DEADLINE_CATEGORY -> dataManager.addTaskUseCase(DeadlineCategoryTask(name = editedTask.name, description = editedTask.description, dateOfCreation = editedTask.dateOfCreation, deadline = editedTask.deadline, category = editedTask.category!!))
            TaskTypes.DEADLINE_PRIORITY -> dataManager.addTaskUseCase(DeadlinePriorityTask(name = editedTask.name, description =  editedTask.description, dateOfCreation = editedTask.dateOfCreation, deadline = editedTask.deadline, priority = editedTask.priority))
            TaskTypes.DEADLINE_PRIORITY_CATEGORY -> dataManager.addTaskUseCase(DeadlinePriorityCategoryTask(name  = editedTask.name, description = editedTask.description, dateOfCreation = editedTask.dateOfCreation, deadline = editedTask.deadline, priority = editedTask.priority, category = editedTask.category!!))
        }

        updateList()
        return status
    }

    fun confirmEditingTask() : Status {
        val status = when(getCurrentType()){
            TaskTypes.PRIORITY -> dataManager.editTaskUseCase(editedTask.id, PriorityTask(name = editedTask.name, description = editedTask.description, dateOfCreation = editedTask.dateOfCreation, priority = editedTask.priority))
            TaskTypes.DEADLINE -> dataManager.editTaskUseCase(editedTask.id, DeadlineTask(name = editedTask.name, description = editedTask.description, dateOfCreation = editedTask.dateOfCreation, deadline = editedTask.deadline))
            TaskTypes.CATEGORY -> dataManager.editTaskUseCase(editedTask.id, CategoryTask(name = editedTask.name, description = editedTask.description, dateOfCreation = editedTask.dateOfCreation, category = editedTask.category!!))
            TaskTypes.DEADLINE_CATEGORY -> dataManager.editTaskUseCase(editedTask.id, DeadlineCategoryTask(name = editedTask.name, description = editedTask.description, dateOfCreation = editedTask.dateOfCreation, deadline = editedTask.deadline, category = editedTask.category!!))
            TaskTypes.DEADLINE_PRIORITY -> dataManager.editTaskUseCase(editedTask.id, DeadlinePriorityTask(name = editedTask.name, description =  editedTask.description, dateOfCreation = editedTask.dateOfCreation, deadline = editedTask.deadline, priority = editedTask.priority))
            TaskTypes.DEADLINE_PRIORITY_CATEGORY -> dataManager.editTaskUseCase(editedTask.id, DeadlinePriorityCategoryTask(name  = editedTask.name, description = editedTask.description, dateOfCreation = editedTask.dateOfCreation, deadline = editedTask.deadline, priority = editedTask.priority, category = editedTask.category!!))
        }

        updateList()
        return status
    }

}