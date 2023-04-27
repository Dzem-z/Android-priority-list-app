package com.example.prioritylist.data

class MainPage(
    private var listOfDeadlineCategoryLists: MutableList<DeadlineCategoryTaskList> = mutableListOf<DeadlineCategoryTaskList>(),
    private var listOfDeadlinePriorityLists: MutableList<DeadlinePriorityTaskList> = mutableListOf<DeadlinePriorityTaskList>(),
    private var listOfDeadlinePriorityCategoryLists: MutableList<DeadlinePriorityCategoryTaskList> = mutableListOf<DeadlinePriorityCategoryTaskList>(),
    private var listOfDeadlineLists: MutableList<DeadlineTaskList> = mutableListOf<DeadlineTaskList>(),
    private var listOfPriorityLists: MutableList<PriorityTaskList> = mutableListOf<PriorityTaskList>(),
    private var listOfCategoryLists: MutableList<CategoryTaskList> = mutableListOf<CategoryTaskList>(),
) {
    var currentListID: Int = 0
        private set
    var currentList: Any? = null
        private set
    var currentType: TaskTypes? = null
        private set

    fun addList(
        type: TaskTypes,
        id: Int,
        name: String
    ) {
        TODO("not yet implemented")
    }
    private fun getListByID(id: Int): Any {
        TODO("not yet implemented")
    }

    fun nextList() {
        TODO("not yet implemented")
    }
    fun prevList() {
        TODO("not yet implemented")
    }
    fun deleteCurrentList(){
        TODO("not yet implemented")
    }

}