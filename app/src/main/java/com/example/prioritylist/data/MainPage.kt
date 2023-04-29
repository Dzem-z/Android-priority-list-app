package com.example.prioritylist.data

import androidx.annotation.VisibleForTesting

class MainPage() {
    private var listOfDeadlineCategoryLists: MutableList<DeadlineCategoryTaskList> = mutableListOf<DeadlineCategoryTaskList>()
    private var listOfDeadlinePriorityLists: MutableList<DeadlinePriorityTaskList> = mutableListOf<DeadlinePriorityTaskList>()
    private var listOfDeadlinePriorityCategoryLists: MutableList<DeadlinePriorityCategoryTaskList> = mutableListOf<DeadlinePriorityCategoryTaskList>()
    private var listOfDeadlineLists: MutableList<DeadlineTaskList> = mutableListOf<DeadlineTaskList>()
    private var listOfPriorityLists: MutableList<PriorityTaskList> = mutableListOf<PriorityTaskList>()
    private var listOfCategoryLists: MutableList<CategoryTaskList> = mutableListOf<CategoryTaskList>()


    var currentListID: Int = 0
        private set
    var currentList: Any? = null
        private set
    var currentType: TaskTypes? = null
        private set

    fun addList(
        type: TaskTypes,
        name: String
    ): Status {
        TODO("not yet implemented")
    }

    @VisibleForTesting
    internal fun getListByID(id: Int): Any {
        TODO("not yet implemented")
    }

    fun changeIDofCurrentList(newId: Int): Status {
        TODO("not yet implemented")
    }
    fun nextList(): Status {
        TODO("not yet implemented")
    }
    fun prevList(): Status {
        TODO("not yet implemented")
    }
    fun deleteCurrentList(): Status {
        TODO("not yet implemented")
    }

}