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

    var currentListID: Int = -1
        private set
    var currentList: TaskList<out Task>? = null
        private set
    var currentType: TaskTypes? = null
        private set

    private fun shift(startingID: Int, value: Int){
        for (i in listOfDeadlineLists) {
            if (i.getID() >= startingID)
                i.changeID(i.getID() + value)
        }
        for (i in listOfPriorityLists) {
            if (i.getID() >= startingID)
                i.changeID(i.getID() + value)
        }
        for (i in listOfCategoryLists){
            if (i.getID() >= startingID)
                i.changeID(i.getID() + value)
        }
        for (i in listOfDeadlinePriorityLists){
            if (i.getID() >= startingID)
                i.changeID(i.getID() + value)
        }
        for (i in listOfDeadlineCategoryLists){
            if (i.getID() >= startingID)
                i.changeID(i.getID() + value)
        }
        for (i in listOfDeadlinePriorityCategoryLists){
            if (i.getID() >= startingID)
                i.changeID(i.getID() + value)
        }
    }


    fun addList(
        type: TaskTypes,
        name: String
    ): Status {
        currentListID += 1
        listIdentifiers.add(currentListID, ListIdentifier(name, type))

        shift(currentListID, 1)

        when (type) {
            TaskTypes.DEADLINE -> listOfDeadlineLists.add(DeadlineTaskList(name, currentListID))
            TaskTypes.PRIORITY -> listOfPriorityLists.add(PriorityTaskList(name, currentListID))
            TaskTypes.CATEGORY -> listOfCategoryLists.add(CategoryTaskList(name, currentListID))
            TaskTypes.DEADLINE_PRIORITY -> listOfDeadlinePriorityLists.add(DeadlinePriorityTaskList(name, currentListID))
            TaskTypes.DEADLINE_CATEGORY -> listOfDeadlineCategoryLists.add(DeadlineCategoryTaskList(name, currentListID))
            TaskTypes.DEADLINE_PRIORITY_CATEGORY -> listOfDeadlinePriorityCategoryLists.add(DeadlinePriorityCategoryTaskList(name, currentListID))
        }

        currentList = getListByID(currentListID)
        currentType = listIdentifiers[currentListID].type

        return Status(StatusCodes.SUCCESS)
    }

    @VisibleForTesting
    internal fun getListByID(id: Int): TaskList<out Task> {
        val returnList = when (listIdentifiers[id].type) {
            TaskTypes.DEADLINE -> listOfDeadlineLists.find{ it.getID() == id }
            TaskTypes.PRIORITY -> listOfPriorityLists.find{ it.getID() == id }
            TaskTypes.CATEGORY -> listOfCategoryLists.find{ it.getID() == id }
            TaskTypes.DEADLINE_PRIORITY -> listOfDeadlinePriorityLists.find{ it.getID() == id }
            TaskTypes.DEADLINE_CATEGORY -> listOfDeadlineCategoryLists.find{ it.getID() == id }
            TaskTypes.DEADLINE_PRIORITY_CATEGORY -> listOfDeadlinePriorityCategoryLists.find{ it.getID() == id }
        } ?: throw IndexOutOfBoundsException()
        return returnList
    }

    fun changeIDofCurrentList(newId: Int): Status {
        var newID = newId
        val list = getListByID(currentListID)

        if (newID > listIdentifiers.size - 1)
            newID = listIdentifiers.size - 1
        return if (list != null) {
            shift(currentListID, -1)
            shift(newID, 1)
            list.changeID(newID)
            listIdentifiers.add(newID, listIdentifiers.removeAt(currentListID))
            currentList = list
            currentListID = newID
            Status(StatusCodes.SUCCESS)

        } else {
            Status(StatusCodes.FAILURE, "error: list with currentID not found.")
        }
    }
    fun nextList(): Status {
        currentListID += 1


        return if (currentListID >= listIdentifiers.size) {

            currentListID -= 1
            Status(StatusCodes.FAILURE, "error: there is no previous list, list not changed")
        } else {
            currentList = getListByID(currentListID)
            currentType = listIdentifiers[currentListID].type
            Status(StatusCodes.SUCCESS)
        }
    }
    fun prevList(): Status {
        currentListID -= 1

        return if (currentListID < 0) {
            currentListID += 1
            Status(StatusCodes.FAILURE, "error: there is no previous list, list not changed")
        } else {
            currentList = getListByID(currentListID)
            currentType = listIdentifiers[currentListID].type
            Status(StatusCodes.SUCCESS)
        }
    }

    fun deleteCurrentList(): Status {
        when (currentType) {
            TaskTypes.DEADLINE -> listOfDeadlineLists.remove(currentList)
            TaskTypes.PRIORITY -> listOfPriorityLists.remove(currentList)
            TaskTypes.CATEGORY -> listOfCategoryLists.remove(currentList)
            TaskTypes.DEADLINE_PRIORITY -> listOfDeadlinePriorityLists.remove(currentList)
            TaskTypes.DEADLINE_CATEGORY -> listOfDeadlineCategoryLists.remove(currentList)
            TaskTypes.DEADLINE_PRIORITY_CATEGORY -> listOfDeadlinePriorityCategoryLists.remove(currentList)
            else -> {return Status(StatusCodes.FAILURE, "error: currentList is null")}
        }
        listIdentifiers.removeAt(currentListID)
        shift(currentListID, -1)
        if (currentListID >= listIdentifiers.size) {
            currentListID -= 1;
        }
        if (currentListID < 0) {
            currentList = null;
            currentType = null;
        } else {
            currentList = getListByID(currentListID)
            currentType = listIdentifiers[currentListID].type
        }
        return Status(StatusCodes.SUCCESS)
    }


}

internal data class ListIdentifier(
    var name: String,
    var type: TaskTypes
)