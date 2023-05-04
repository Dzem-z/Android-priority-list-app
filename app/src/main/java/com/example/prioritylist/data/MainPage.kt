package com.example.prioritylist.data

import androidx.annotation.VisibleForTesting

class MainPage() {
    private val listOfDeadlineCategoryLists: MutableList<DeadlineCategoryTaskList> = mutableListOf<DeadlineCategoryTaskList>()
    private val listOfDeadlinePriorityLists: MutableList<DeadlinePriorityTaskList> = mutableListOf<DeadlinePriorityTaskList>()
    private val listOfDeadlinePriorityCategoryLists: MutableList<DeadlinePriorityCategoryTaskList> = mutableListOf<DeadlinePriorityCategoryTaskList>()
    private val listOfDeadlineLists: MutableList<DeadlineTaskList> = mutableListOf<DeadlineTaskList>()
    private val listOfPriorityLists: MutableList<PriorityTaskList> = mutableListOf<PriorityTaskList>()
    private val listOfCategoryLists: MutableList<CategoryTaskList> = mutableListOf<CategoryTaskList>()

    private val listIdentifiers = mutableListOf<ListIdentifier>()

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

data class ListIdentifier(
    var name: String,
    var id: Int,
    var type: TaskTypes
)