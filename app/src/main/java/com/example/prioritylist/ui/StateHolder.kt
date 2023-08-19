package com.example.prioritylist.ui

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import com.example.prioritylist.data.backend.ModifiableTask
import com.example.prioritylist.data.backend.Status
import com.example.prioritylist.data.backend.Task
import com.example.prioritylist.data.backend.TaskTypes
import com.example.prioritylist.data.backend.MainPage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.prioritylist.data.backend.HistoryTask
import com.example.prioritylist.data.database.ListRepository
import com.example.prioritylist.data.database.MainRepository
import com.example.prioritylist.data.backend.UserPreferencesRepository
import com.example.prioritylist.domain.DataManager
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

/**
* [StateHolder] is main ViewModel for priorityList app, it contains two subclasses: ReadViewModel and UIViewModel.
* It provides all state-based functionality and also serves as database communicator
*
* @param listRepository is a reference to [listRepository] class
* @param mainRepository is a reference to [MainRepository] class
* @param mainPageRepository is a reference to [MainPageRepository] class
 * @param userPreferencesRepository is a reference to [UserPreferencesRepository] class
* all of these should be initialized with [AppViewModelProvider]
*  */

class StateHolder(
    private val listRepository: ListRepository,
    private val mainRepository: MainRepository,
    private val mainPageRepository: MainPage,
    private val userPreferencesRepository: UserPreferencesRepository
    ) : ViewModel() {

    /**
     * [UiViewModel] is responsible for all state-related actions that do not require any database data nor any backend interference
     */

    class UiViewModel: ViewModel() {
        var editedTask by mutableStateOf(ModifiableTask())  //an instance of state of currently edited Task

        var visible by mutableStateOf(true) //state used in [animatedVisibility], determines which list should be displayed

        var currentListName by mutableStateOf("")   //an actual list name

        var newListName by mutableStateOf("")   //addList textField state name
        var selectedTypeText by mutableStateOf("priority-based tasks")  //addList textField state type text
        var selectedType by mutableStateOf<TaskTypes>(TaskTypes.PRIORITY)   //addList textField state type enum

        private var badName by mutableStateOf(false)    //a flag indicating if name entered by the user is invalid


        var duplicatedName by mutableStateOf(false) //same name already in list
        var emptyName by mutableStateOf(false)  //emptyName
        var priorityOverflowError by mutableStateOf(false) //if priority number is too big
        var settingsOverflowError by mutableStateOf(false) //if settings number is too big

        var taskBottomSheetExpanded by mutableStateOf(true) //a state flag indicating if bottomSheet is expanded


        fun setDuplicatedTaskError() {
            duplicatedName = true
            badName = true
        }

        fun setEmptyNameError() {
            badName = true
            emptyName = true
        }

        fun setOverflowError() {
            priorityOverflowError = true
        }

        //checks for overflowError and sets overflowError flag accordingly
        fun checkForOverflowError(str: String): Boolean {
            if (str.length > 3)
                priorityOverflowError = true
            else
                priorityOverflowError = false
            return priorityOverflowError
        }

        //clears all errors
        fun clearNameErrorFlags() {
            badName = false
            emptyName = false
            duplicatedName = false
        }

        fun isDuplicatedTask(): Boolean {
            return badName
        }

        //clears all fields of [EditedTask]
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

    val UI = UiViewModel()  //an instance of [UiViewModel]

    /**
    * [ReadViewModel] is an inner class ViewModel that is responsible for asking the data layer for information about lists
    * */

    inner class ReadViewModel : ViewModel() {
        /*
        * these duplicated fields serve as state switch between lists in [animatedVisibility]
        * */

        var firstType by mutableStateOf<TaskTypes>(TaskTypes.PRIORITY)
        var secondType by mutableStateOf<TaskTypes>(TaskTypes.PRIORITY)

        var firstList by mutableStateOf<MutableList<out Task>>(mutableListOf<Task>())
        var secondList by mutableStateOf<MutableList<out Task>>(mutableListOf<Task>())

        var firstHistoryList by mutableStateOf<MutableList<out HistoryTask<out Task>>>(mutableListOf<HistoryTask<Task>>())
        var secondHistoryList by mutableStateOf<MutableList<out HistoryTask<out Task>>>(mutableListOf<HistoryTask<Task>>())

        var isPrevList by mutableStateOf(false)
        var isNextList by mutableStateOf(false)

        var currentListIndex = MutableStateFlow(0)  //an pointer on currently focused list
        var index = currentListIndex.asStateFlow()  //an instance of StateFlow of currentListIndex
            private set

        var isStorageEmpty by mutableStateOf(false)


        //switches between first and second list
        internal fun incrementIndex() {
            currentListIndex.value = (currentListIndex.value + 1) % 2
            index = currentListIndex.asStateFlow()
            UI.visible = !UI.visible
        }

        fun isEmpty(): Boolean {
            return dataManager.isEmptyUseCase()
        }

        //returns true if there is only single list left
        fun isAlmostEmpty(): Boolean {
            return !isNextListPresent() && !isPrevListPresent()
        }

        init{
            updateList()
        }

        //assigns list to the variable exposing it to the UI
        @VisibleForTesting
        internal fun setList(list: MutableList<out Task>) {
            if (currentListIndex.value == 0){
                firstList = list
            } else {
                secondList = list
            }
        }

        //assigns list to the variable exposing it to the UI
        internal fun setHistoryList(list: MutableList<out HistoryTask<out Task>>){
            if(currentListIndex.value == 0){
                firstHistoryList = list
            } else {
                secondHistoryList = list
            }
        }

        //updates all of the credentials of a single list
        fun updateList() {
            setList(dataManager.getListUseCase())
            setHistoryList(dataManager.getHistoryListUseCase())
            isPrevList = isPrevListPresent()
            isNextList = isNextListPresent()
            UI.currentListName = dataManager.getNameUseCase()
            isStorageEmpty = dataManager.isStorageEmptyUseCase()
            setCurrentType(dataManager.getCurrentType()?: TaskTypes.PRIORITY)
        }

        fun getList(): MutableList<out Task> {
            return if (currentListIndex.value == 0)
                firstList
            else
                secondList
        }

        fun getHistoryList(): MutableList<out HistoryTask<out Task>> {
            return dataManager.getHistoryListUseCase()
        }

        fun getCurrentType(): TaskTypes {
            return if (currentListIndex.value == 0)
                firstType
            else
                secondType
        }

        fun setCurrentType(type: TaskTypes) {
            if (currentListIndex.value == 0)
                firstType = type
            else
                secondType = type
        }

        fun getName(): String{
            UI.currentListName = dataManager.getNameUseCase()
            return UI.currentListName
        }

        fun getDateOfCreation(): Date?{
            return dataManager.getDateOfCreationUseCase()
        }

    }

    private val dataManager = DataManager(mainPage = mainPageRepository, listRepository = listRepository, mainRepository = mainRepository)  //an instance of DataManager

    val Read = ReadViewModel()

    /**
     * [SettingsManager] manages [UserPreferencesRepository]. It bridges UI with [UserPreferencesRepository].
     * @param userPreferencesRepository is a reference to [UserPreferencesRepository]
     */

    class SettingsManager(
        private val userPreferencesRepository: UserPreferencesRepository
    ): ViewModel() {

        fun saveDeadlinePeriodDays(period: UInt) {
            viewModelScope.launch {
                userPreferencesRepository.saveDeadlinePeriodDays(period)
            }
        }

        val deadlinePeriodDays: StateFlow<UInt> = userPreferencesRepository.deadlinePeriodDays
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = userPreferencesRepository.THREE_DAYS
            )
    }

    val Settings = SettingsManager(userPreferencesRepository)


    fun setName(): Status {
        return dataManager.changeNameUseCase(UI.currentListName)
    }


    //called whenever user enters addTask Screen
    fun onAddTask(){
        UI.resetEditedTask()
    }

    //called whenever user enters editTask Screen
    fun onEditTask(){
    }

    //called when task is deleted
    suspend fun onDeleteTask(): Status {
        val status = dataManager.deleteTaskUseCase(Read.getCurrentType().toTask(UI.editedTask))

        Read.updateList()
        return status
    }


    suspend fun onUndo(){
        dataManager.undoUseCase()
        Read.updateList()
    }

    //loads next list to the memory
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

    //loads previous list to the memory
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

    //true if next list is present in the list
    fun isNextListPresent(): Boolean{
        return if(dataManager.nextListUseCase() != null){
            dataManager.prevListUseCase()
            true
        } else {
            false
        }
    }

    //true if previous list is present in the list
    fun isPrevListPresent(): Boolean{
        return if(dataManager.prevListUseCase() != null){
            dataManager.nextListUseCase()
            true
        } else {
            false
        }
    }

    suspend fun addList() {
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



    suspend fun removeList() {
        val returnedType = dataManager.deleteCurrentListUseCase()
        if( returnedType != null ){
            Read.incrementIndex()
            Read.setCurrentType(returnedType)
            Read.updateList()
        } else {
            /*TODO(empty screen)*/
        }
    }


    //swaps current list with previous one
    suspend fun swapWithLeft() {
        dataManager.changeIDUseCase(dataManager.getIDUseCase() - 1)
        Read.updateList()
    }

    //swaps current list with next one
    suspend fun swapWithRight() {
        dataManager.changeIDUseCase(dataManager.getIDUseCase() + 1)
        Read.updateList()
    }

    //called when user completes task
    suspend fun onDone(task: Task) {
        dataManager.moveToHistoryUseCase(task, Calendar.getInstance().time)
        Read.updateList()
    }

    suspend fun addTask(): Status {
        val status = dataManager.addTaskUseCase(Read.getCurrentType().toTask(UI.editedTask))

        Read.updateList()
        return status
    }

    suspend fun confirmEditingTask() : Status {
        val status = dataManager.editTaskUseCase(UI.editedTask.id, Read.getCurrentType().toTask(UI.editedTask))

        Read.updateList()
        return status
    }

}