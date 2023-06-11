package com.example.prioritylist.domain

import com.example.prioritylist.data.backend.*
import com.example.prioritylist.data.backend.MainPage
import com.example.prioritylist.data.backend.Settings
import com.example.prioritylist.data.backend.Status
import com.example.prioritylist.data.backend.StatusCodes
import com.example.prioritylist.data.backend.TaskTypes
import java.util.Date

/*
TODO(comments)
 */

class DataManager(
    private val mainPage: MainPage = MainPage(),
    private val settings: Settings = Settings()
) {


    fun addTaskUseCase(task: Task): Status {
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

    fun isEmptyUseCase(): Boolean {
        return if (mainPage.currentList == null) {
            true
        } else {
            false
        }
    }

    fun editTaskUseCase(oldId: Int, task: Task): Status {
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

    fun deleteTaskUseCase(task: Task): Status {
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

    fun moveToHistoryUseCase(task: Task, dateOfCompletion: Date): Status {
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

    fun getListUseCase(): MutableList<out Task> {
        return mainPage.currentList?.getList()?: mutableListOf<Task>()
    }

    fun getNameUseCase(): String{
        val list = mainPage.currentList
        if (list != null)
            return list.getName()
        else
            return ""
    }

    fun getDateOfCreationUseCase(): Date{
        val list = mainPage.currentList
        if (list != null)
            return list.getDateOfCreation()
        else
            throw NullPointerException()
    }

    fun getHistoryListUseCase(): MutableList<out HistoryTask<out Task>> {
        val list = mainPage.currentList
        if (list == null){
            return mutableListOf<HistoryTask<Task>>()
        } else {
            return list.history.getList()
        }
    }

    fun deleteFromHistoryUseCase(name: String): Status {
        TODO("Not yet implemented")
    }

    fun deleteUntilUseCase(date: Date): Status {
        TODO("Not yet implemented")
    }

    fun changeNameUseCase(name: String): Status {
        val list = mainPage.currentList
        if (list != null) {
            list.changeName(name)
        } else {
            throw NullPointerException()
        }
        return Status(StatusCodes.SUCCESS)
    }

    fun undoUseCase(): Status {
        return mainPage.undo()
    }

    fun isStorageEmptyUseCase(): Boolean {
        return mainPage.isStorageEmpty()
    }

    fun changeIDUseCase(newId: Int) {
        mainPage.changeIDofCurrentList(newId)
    }

    fun getIDUseCase(): Int {
        return mainPage.currentListID
    }

    fun prevListUseCase(): TaskTypes? {
        if(mainPage.prevList().code == StatusCodes.FAILURE)
            return null
        else {
            return mainPage.currentType
        }
    }

    fun nextListUseCase(): TaskTypes? {
        if(mainPage.nextList().code == StatusCodes.FAILURE)
            return null
        else {
            return mainPage.currentType
        }
    }

    fun addListUseCase(id: Int, name: String, type: TaskTypes, dateOfCreation: Date): TaskTypes {
        mainPage.addList(type, name, dateOfCreation)
        mainPage.changeIDofCurrentList(id)
        return type
    }

    fun deleteCurrentListUseCase(): TaskTypes? {
        if (mainPage.deleteCurrentList().code != StatusCodes.SUCCESS)
            return null
        else
            return mainPage.currentType
    }

}