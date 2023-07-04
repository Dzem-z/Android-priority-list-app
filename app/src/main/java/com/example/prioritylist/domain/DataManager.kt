package com.example.prioritylist.domain

import com.example.prioritylist.data.backend.*
import com.example.prioritylist.data.backend.MainPage
import com.example.prioritylist.data.backend.Settings
import com.example.prioritylist.data.backend.Status
import com.example.prioritylist.data.backend.StatusCodes
import com.example.prioritylist.data.backend.TaskTypes
import com.example.prioritylist.data.database.ListRepository
import com.example.prioritylist.data.database.MainRepository
import java.util.Date

/**
 * [DataManager] is a class arranged in a domain layout that simplifies the applications bussiness logic
 * @param mainRepository is a link to main repository
 * @param listRepository is a link to list repository
 * @param mainPage is an instance of MainPage class that manages all lists
 * @param settings is an instance of Settings class //TODO("not yet implemented")
 * */

class DataManager(
    private val mainRepository: MainRepository,
    private val listRepository: ListRepository,
    private val mainPage: MainPage = MainPage(listRepository, mainRepository),
    private val settings: Settings = Settings()
) {

    //suspended function that adds task to the current list
    suspend fun addTaskUseCase(task: Task): Status {
        val list = mainPage.currentList
        val currentType = mainPage.currentType
        var status: Status
        if (currentType != null && list != null) {
           status =  when (currentType) {
                TaskTypes.PRIORITY -> (list as PriorityTaskList).addTask(task as PriorityTask)
                TaskTypes.CATEGORY -> (list as CategoryTaskList).addTask(task as CategoryTask)
                TaskTypes.DEADLINE -> (list as DeadlineTaskList).addTask(task as DeadlineTask)
                TaskTypes.DEADLINE_PRIORITY -> (list as DeadlinePriorityTaskList).addTask(task as DeadlinePriorityTask)
                TaskTypes.DEADLINE_PRIORITY_CATEGORY -> (list as DeadlinePriorityCategoryTaskList).addTask(task as DeadlinePriorityCategoryTask)
                TaskTypes.DEADLINE_CATEGORY -> (list as DeadlineCategoryTaskList).addTask(task as DeadlineCategoryTask)
            }
        } else{
            return Status(StatusCodes.FAILURE, "error: adding task to null list")
        }
        return status
    }

    //returns true if there is no list in database, false otherwise
    fun isEmptyUseCase(): Boolean {
        return if (mainPage.currentList == null) {
            true
        } else {
            false
        }
    }


    //replaces task identified by oldId with new task in current list
    suspend fun editTaskUseCase(oldId: Int, task: Task): Status {
        val list = mainPage.currentList
        val currentType = mainPage.currentType
        if (currentType != null && list != null) {
            when (currentType) {
                TaskTypes.PRIORITY -> (list as PriorityTaskList).editTask(oldId, task as PriorityTask)
                TaskTypes.CATEGORY -> (list as CategoryTaskList).editTask(oldId, task as CategoryTask)
                TaskTypes.DEADLINE -> (list as DeadlineTaskList).editTask(oldId, task as DeadlineTask)
                TaskTypes.DEADLINE_PRIORITY -> (list as DeadlinePriorityTaskList).editTask(oldId, task as DeadlinePriorityTask)
                TaskTypes.DEADLINE_PRIORITY_CATEGORY -> (list as DeadlinePriorityCategoryTaskList).editTask(oldId, task as DeadlinePriorityCategoryTask)
                TaskTypes.DEADLINE_CATEGORY -> (list as DeadlineCategoryTaskList).editTask(oldId, task as DeadlineCategoryTask)
            }
        } else{
            return Status(StatusCodes.FAILURE, "error: adding task to null list")
        }
        return Status(StatusCodes.SUCCESS)
    }


    //deletes task from current list
    suspend fun deleteTaskUseCase(task: Task): Status {
        val list = mainPage.currentList
        val currentType = mainPage.currentType
        if (currentType != null && list != null) {
            when (currentType) {
                TaskTypes.PRIORITY -> (list as PriorityTaskList).deleteTask(task as PriorityTask)
                TaskTypes.CATEGORY -> (list as CategoryTaskList).deleteTask(task as CategoryTask)
                TaskTypes.DEADLINE -> (list as DeadlineTaskList).deleteTask(task as DeadlineTask)
                TaskTypes.DEADLINE_PRIORITY -> (list as DeadlinePriorityTaskList).deleteTask(task as DeadlinePriorityTask)
                TaskTypes.DEADLINE_PRIORITY_CATEGORY -> (list as DeadlinePriorityCategoryTaskList).deleteTask(task as DeadlinePriorityCategoryTask)
                TaskTypes.DEADLINE_CATEGORY -> (list as DeadlineCategoryTaskList).deleteTask(task as DeadlineCategoryTask)
            }
        } else{
            return Status(StatusCodes.FAILURE, "error: adding task to null list")
        }
        return Status(StatusCodes.SUCCESS)
    }

    //removes task from lists and appends task and dateOfCompletion to history in current list
    suspend fun moveToHistoryUseCase(task: Task, dateOfCompletion: Date): Status {
        val list = mainPage.currentList
        val currentType = mainPage.currentType
        var status = Status(StatusCodes.FAILURE)
        var hstatus = Status(StatusCodes.SUCCESS)
        if (currentType != null && list != null) {
            when(currentType) {
                TaskTypes.PRIORITY -> {
                    status = (list as PriorityTaskList).deleteTask(task as PriorityTask)
                    hstatus = list.history.pushTask(task, dateOfCompletion)
                }
                TaskTypes.CATEGORY -> {
                    status = (list as CategoryTaskList).deleteTask(task as CategoryTask)
                    hstatus = list.history.pushTask(task, dateOfCompletion)
                }
                TaskTypes.DEADLINE -> {
                    status = (list as DeadlineTaskList).deleteTask(task as DeadlineTask)
                    hstatus = list.history.pushTask(task, dateOfCompletion)
                }
                TaskTypes.DEADLINE_PRIORITY -> {
                    status = (list as DeadlinePriorityTaskList).deleteTask(task as DeadlinePriorityTask)
                    hstatus = list.history.pushTask(task, dateOfCompletion)
                }
                TaskTypes.DEADLINE_PRIORITY_CATEGORY -> {
                    status = (list as DeadlinePriorityCategoryTaskList).deleteTask(task as DeadlinePriorityCategoryTask)
                    hstatus = list.history.pushTask(task, dateOfCompletion)
                }
                TaskTypes.DEADLINE_CATEGORY -> {
                    status = (list as DeadlineCategoryTaskList).deleteTask(task as DeadlineCategoryTask)
                    hstatus = list.history.pushTask(task, dateOfCompletion)
                }
            }
        }
        if(status.code == StatusCodes.SUCCESS && hstatus.code == StatusCodes.SUCCESS)
            return Status(StatusCodes.SUCCESS)
        else
            return Status(StatusCodes.FAILURE)
    }

    //returns List filled with tasks from the current list
    fun getListUseCase(): MutableList<out Task> {
        return mainPage.currentList?.getList()?: mutableListOf<Task>()
    }

    //returns name of current list
    fun getNameUseCase(): String{
        val list = mainPage.currentList
        if (list != null)
            return list.getName()
        else
            return ""
    }

    //returns dateOfCreation of current list
    fun getDateOfCreationUseCase(): Date?{
        val list = mainPage.currentList
        if (list != null)
            return list.getDateOfCreation()
        else
            return null
    }

    //returns List filled with history tasks from the current list
    fun getHistoryListUseCase(): MutableList<out HistoryTask<out Task>> {
        val list = mainPage.currentList
        if (list == null){
            return mutableListOf<HistoryTask<Task>>()
        } else {
            return list.history.getList()
        }
    }

    //deletes task identified by name from history of current list
    fun deleteFromHistoryUseCase(name: String): Status {
        TODO("Not yet implemented")
    }

    //deletes all tasks with dates until date
    fun deleteUntilUseCase(date: Date): Status {
        TODO("Not yet implemented")
    }

    //changes the name of current list to name
    fun changeNameUseCase(name: String): Status {
        val list = mainPage.currentList
        if (list != null) {
            list.changeName(name)
        } else {
            throw NullPointerException()
        }
        return Status(StatusCodes.SUCCESS)
    }


    //undo last operation
    suspend fun undoUseCase(): Status {
        return mainPage.undo()
    }

    //returns true if storage is empty (there is no actions in storage), false otherwise
    fun isStorageEmptyUseCase(): Boolean {
        return mainPage.isStorageEmpty()
    }

    //changes the id of current list to newId
    suspend fun changeIDUseCase(newId: Int) {
        mainPage.changeIDofCurrentList(newId)
    }

    //returns the id of current list
    fun getIDUseCase(): Int {
        return mainPage.currentListID
    }

    //switches to list previous to the current one, returns null if isn't present
    fun prevListUseCase(): TaskTypes? {
        if(mainPage.prevList().code == StatusCodes.FAILURE)
            return null
        else {
            return mainPage.currentType
        }
    }

    //switches to list next to the current one, returns null if isn't present
    fun nextListUseCase(): TaskTypes? {
        if(mainPage.nextList().code == StatusCodes.FAILURE)
            return null
        else {
            return mainPage.currentType
        }
    }

    //adds list with specified arguments after current list and sets it as current
    suspend fun addListUseCase(id: Int, name: String, type: TaskTypes, dateOfCreation: Date): TaskTypes {
        mainPage.addList(type, name, dateOfCreation)
        mainPage.changeIDofCurrentList(id)
        return type
    }

    //deletes current list and switches to next one (or previous if next not present)
    suspend fun deleteCurrentListUseCase(): TaskTypes? {
        if (mainPage.deleteCurrentList().code != StatusCodes.SUCCESS)
            return null
        else
            return mainPage.currentType
    }

}