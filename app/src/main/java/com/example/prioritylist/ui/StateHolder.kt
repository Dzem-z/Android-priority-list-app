package com.example.prioritylist.ui

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import com.example.prioritylist.data.backend.ModifiableTask
import com.example.prioritylist.data.backend.Status
import com.example.prioritylist.data.backend.Task
import com.example.prioritylist.data.backend.TaskTypes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.prioritylist.data.backend.Category
import com.example.prioritylist.data.backend.CategoryTask
import com.example.prioritylist.data.backend.CategoryTaskList
import com.example.prioritylist.data.backend.DeadlineCategoryTask
import com.example.prioritylist.data.backend.DeadlineCategoryTaskList
import com.example.prioritylist.data.backend.DeadlinePriorityCategoryTask
import com.example.prioritylist.data.backend.DeadlinePriorityCategoryTaskList
import com.example.prioritylist.data.backend.DeadlinePriorityTask
import com.example.prioritylist.data.backend.DeadlineTask
import com.example.prioritylist.data.backend.HistoryTask
import com.example.prioritylist.data.database.ListRepository
import com.example.prioritylist.data.database.MainRepository
import com.example.prioritylist.data.database.OfflineListRepository
import com.example.prioritylist.data.backend.PriorityTask
import com.example.prioritylist.data.backend.StatusCodes
import com.example.prioritylist.domain.DataManager
import java.util.Calendar
import java.util.Date

/*
TODO(comments)
 */

class StateHolder(
    private val listRepository: ListRepository,
    private val mainRepository: MainRepository
    ) : ViewModel() {

    class UiViewModel: ViewModel() {
        var editedTask by mutableStateOf(ModifiableTask())

        var visible by mutableStateOf(true)

        var currentListName by mutableStateOf("")

        var newListName by mutableStateOf("")
        var selectedTypeText by mutableStateOf("priority-based tasks")
        var selectedType by mutableStateOf<TaskTypes>(TaskTypes.PRIORITY)

        private var badName by mutableStateOf(false)


        var duplicatedName by mutableStateOf(false)
        var emptyName by mutableStateOf(false)

        var taskBottomSheetExpanded by mutableStateOf(true)

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

        fun resetAddListParameters() {
            selectedType = TaskTypes.PRIORITY
            newListName = ""
            selectedTypeText = "priority-based tasks"
        }
    }

    val UI = UiViewModel()

    inner class ReadViewModel : ViewModel() {
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

        var isStorageEmpty by mutableStateOf(false)

        internal fun incrementIndex() {
            currentListIndex.value = (currentListIndex.value + 1) % 2
            index = currentListIndex.asStateFlow()
            UI.visible = !UI.visible
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
            UI.currentListName = dataManager.getNameUseCase()
            isStorageEmpty = dataManager.isStorageEmptyUseCase()
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

        fun getCurrentType(): TaskTypes {
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
            UI.currentListName = dataManager.getNameUseCase()
            return UI.currentListName
        }

        fun getDateOfCreation(): Date{
            return dataManager.getDateOfCreationUseCase()
        }

    }

    private val dataManager = DataManager()

    val Read = ReadViewModel()




    fun setName(): Status {
        return dataManager.changeNameUseCase(UI.currentListName)
    }

    fun onAddTask(){
        UI.resetEditedTask()
    }

    fun onEditTask(){
    }

    fun onDeleteTask(): Status {
        val status = when(Read.getCurrentType()){
            TaskTypes.PRIORITY -> dataManager.deleteTaskUseCase(PriorityTask(name = UI.editedTask.name, description = UI.editedTask.description, dateOfCreation = UI.editedTask.dateOfCreation, priority = UI.editedTask.priority))
            TaskTypes.DEADLINE -> dataManager.deleteTaskUseCase(DeadlineTask(name = UI.editedTask.name, description = UI.editedTask.description, dateOfCreation = UI.editedTask.dateOfCreation, deadline = UI.editedTask.deadline))
            TaskTypes.CATEGORY -> dataManager.deleteTaskUseCase(CategoryTask(name = UI.editedTask.name, description = UI.editedTask.description, dateOfCreation = UI.editedTask.dateOfCreation, category = UI.editedTask.category!!))
            TaskTypes.DEADLINE_CATEGORY -> dataManager.deleteTaskUseCase(DeadlineCategoryTask(name = UI.editedTask.name, description = UI.editedTask.description, dateOfCreation = UI.editedTask.dateOfCreation, deadline = UI.editedTask.deadline, category = UI.editedTask.category!!))
            TaskTypes.DEADLINE_PRIORITY -> dataManager.deleteTaskUseCase(DeadlinePriorityTask(name = UI.editedTask.name, description =  UI.editedTask.description, dateOfCreation = UI.editedTask.dateOfCreation, deadline = UI.editedTask.deadline, priority = UI.editedTask.priority))
            TaskTypes.DEADLINE_PRIORITY_CATEGORY -> dataManager.deleteTaskUseCase(DeadlinePriorityCategoryTask(name  = UI.editedTask.name, description = UI.editedTask.description, dateOfCreation = UI.editedTask.dateOfCreation, deadline = UI.editedTask.deadline, priority = UI.editedTask.priority, category = UI.editedTask.category!!))
        }

        Read.updateList()
        return status
    }
    fun onUndo(){
        dataManager.undoUseCase()
        Read.updateList()
    }

    fun nextList(){
        val returnedType = dataManager.nextListUseCase()
        if(returnedType == null){
            throw Exception()
        } else {
            Read.incrementIndex()
            Read.setCurrentType(returnedType)
            Read.updateList()
        }
    }


    fun prevList(){
        val returnedType = dataManager.prevListUseCase()
        if(returnedType == null){
            throw Exception()
        } else {
            Read.incrementIndex()
            Read.setCurrentType(returnedType)
            Read.updateList()
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
            UI.newListName,
            UI.selectedType,
            Calendar.getInstance().time
        )

        Read.incrementIndex()
        Read.updateList()
        Read.setCurrentType(UI.selectedType)
        UI.resetAddListParameters()
    }



    fun removeList() {
        val returnedType = dataManager.deleteCurrentListUseCase()
        if( returnedType != null ){
            Read.incrementIndex()
            Read.setCurrentType(returnedType)
            Read.updateList()
        } else {
            /*TODO(empty screen)*/
        }
    }

    fun swapWithLeft() {
        dataManager.changeIDUseCase(dataManager.getIDUseCase() - 1)
        Read.updateList()
    }

    fun swapWithRight() {
        dataManager.changeIDUseCase(dataManager.getIDUseCase() + 1)
        Read.updateList()
    }

    fun onDelete(task: Task) {

    }

    fun onDone(task: Task) {
        //dataManager.moveToHistoryUseCase(task)
        Read.updateList()
    }
    fun updateTask(task: Task){
        TODO("Not yet implemented")
    }
    fun addTask(): Status {

        val status = when(Read.getCurrentType()){
            TaskTypes.PRIORITY -> dataManager.addTaskUseCase(PriorityTask(name = UI.editedTask.name, description = UI.editedTask.description, dateOfCreation = UI.editedTask.dateOfCreation, priority = UI.editedTask.priority))
            TaskTypes.DEADLINE -> dataManager.addTaskUseCase(DeadlineTask(name = UI.editedTask.name, description = UI.editedTask.description, dateOfCreation = UI.editedTask.dateOfCreation, deadline = UI.editedTask.deadline))
            TaskTypes.CATEGORY -> dataManager.addTaskUseCase(CategoryTask(name = UI.editedTask.name, description = UI.editedTask.description, dateOfCreation = UI.editedTask.dateOfCreation, category = UI.editedTask.category!!))
            TaskTypes.DEADLINE_CATEGORY -> dataManager.addTaskUseCase(DeadlineCategoryTask(name = UI.editedTask.name, description = UI.editedTask.description, dateOfCreation = UI.editedTask.dateOfCreation, deadline = UI.editedTask.deadline, category = UI.editedTask.category!!))
            TaskTypes.DEADLINE_PRIORITY -> dataManager.addTaskUseCase(DeadlinePriorityTask(name = UI.editedTask.name, description =  UI.editedTask.description, dateOfCreation = UI.editedTask.dateOfCreation, deadline = UI.editedTask.deadline, priority = UI.editedTask.priority))
            TaskTypes.DEADLINE_PRIORITY_CATEGORY -> dataManager.addTaskUseCase(DeadlinePriorityCategoryTask(name  = UI.editedTask.name, description = UI.editedTask.description, dateOfCreation = UI.editedTask.dateOfCreation, deadline = UI.editedTask.deadline, priority = UI.editedTask.priority, category = UI.editedTask.category!!))
        }

        Read.updateList()
        return status
    }

    fun confirmEditingTask() : Status {
        val status = when(Read.getCurrentType()){
            TaskTypes.PRIORITY -> dataManager.editTaskUseCase(UI.editedTask.id, PriorityTask(name = UI.editedTask.name, description = UI.editedTask.description, dateOfCreation = UI.editedTask.dateOfCreation, priority = UI.editedTask.priority))
            TaskTypes.DEADLINE -> dataManager.editTaskUseCase(UI.editedTask.id, DeadlineTask(name = UI.editedTask.name, description = UI.editedTask.description, dateOfCreation = UI.editedTask.dateOfCreation, deadline = UI.editedTask.deadline))
            TaskTypes.CATEGORY -> dataManager.editTaskUseCase(UI.editedTask.id, CategoryTask(name = UI.editedTask.name, description = UI.editedTask.description, dateOfCreation = UI.editedTask.dateOfCreation, category = UI.editedTask.category!!))
            TaskTypes.DEADLINE_CATEGORY -> dataManager.editTaskUseCase(UI.editedTask.id, DeadlineCategoryTask(name = UI.editedTask.name, description = UI.editedTask.description, dateOfCreation = UI.editedTask.dateOfCreation, deadline = UI.editedTask.deadline, category = UI.editedTask.category!!))
            TaskTypes.DEADLINE_PRIORITY -> dataManager.editTaskUseCase(UI.editedTask.id, DeadlinePriorityTask(name = UI.editedTask.name, description =  UI.editedTask.description, dateOfCreation = UI.editedTask.dateOfCreation, deadline = UI.editedTask.deadline, priority = UI.editedTask.priority))
            TaskTypes.DEADLINE_PRIORITY_CATEGORY -> dataManager.editTaskUseCase(UI.editedTask.id, DeadlinePriorityCategoryTask(name  = UI.editedTask.name, description = UI.editedTask.description, dateOfCreation = UI.editedTask.dateOfCreation, deadline = UI.editedTask.deadline, priority = UI.editedTask.priority, category = UI.editedTask.category!!))
        }

        Read.updateList()
        return status
    }

}