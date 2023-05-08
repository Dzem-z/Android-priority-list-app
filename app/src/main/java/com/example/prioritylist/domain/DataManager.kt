package com.example.prioritylist.domain

import com.example.prioritylist.data.*
import java.util.Date

/*
TODO(comments)
 */

class DataManager(
    private val mainPage: MainPage = MainPage(),
    private val settings: Settings = Settings()
) {

    fun prevListUseCase(): TaskTypes {
        TODO("Not yet implemented")
    }
    fun addTaskUseCase(task: Task): Status {
        val list = mainPage.currentList
        val currentType = mainPage.currentType
        if (currentType != null && list != null) {
            when (currentType) {
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
        return Status(StatusCodes.SUCCESS)
    }
    fun editTaskUseCase(task: Task): Status {
        TODO("Not yet implemented")
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
    fun moveToHistoryUseCase(task: Task): Status {
        TODO("Not yet implemented")
    }
    fun getListUseCase(): MutableList<out Task> {
        return mainPage.currentList?.getList()?: mutableListOf<Task>()
    }
    fun getNameUseCase(): String{
        TODO("Not yet implemented")
    }
    fun getHistoryListUseCase(): MutableList<HistoryTask<*>> {
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
        mainPage.addList(type, name)
        mainPage.changeIDofCurrentList(id)
    }
    fun deleteCurrentListUseCase(): Status {
        TODO("Not yet implemented")
    }

}