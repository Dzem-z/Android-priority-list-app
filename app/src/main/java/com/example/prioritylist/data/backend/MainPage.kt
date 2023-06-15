package com.example.prioritylist.data.backend

import androidx.annotation.VisibleForTesting
import com.example.prioritylist.data.backend.CategoryTaskList
import com.example.prioritylist.data.backend.DeadlineCategoryTaskList
import com.example.prioritylist.data.backend.DeadlinePriorityCategoryTaskList
import com.example.prioritylist.data.backend.DeadlinePriorityTaskList
import com.example.prioritylist.data.backend.DeadlineTask
import com.example.prioritylist.data.backend.DeadlineTaskList
import com.example.prioritylist.data.backend.PriorityTask
import com.example.prioritylist.data.backend.PriorityTaskList
import com.example.prioritylist.data.backend.Status
import com.example.prioritylist.data.backend.StatusCodes
import com.example.prioritylist.data.backend.Task
import com.example.prioritylist.data.backend.TaskList
import com.example.prioritylist.data.backend.TaskTypes
import com.example.prioritylist.data.database.ListEntity
import com.example.prioritylist.data.database.ListRepository
import com.example.prioritylist.data.database.MainRepository
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Calendar
import java.util.Date

class MainPage(
    private val listRepository: ListRepository,
    private val mainRepository: MainRepository
) {
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



    init{

    }


    suspend fun addList(
        type: TaskTypes,
        name: String,
        dateOfCreation: Date
    ): Status {
        currentListID += 1
        listIdentifiers.add(currentListID, ListIdentifier(name, type))

        shift(currentListID, 1)

        when (type) {
            TaskTypes.DEADLINE -> listOfDeadlineLists.add(DeadlineTaskList(name, currentListID, dateOfCreation))
            TaskTypes.PRIORITY -> listOfPriorityLists.add(PriorityTaskList(name, currentListID, dateOfCreation))
            TaskTypes.CATEGORY -> listOfCategoryLists.add(CategoryTaskList(name, currentListID, dateOfCreation))
            TaskTypes.DEADLINE_PRIORITY -> listOfDeadlinePriorityLists.add(DeadlinePriorityTaskList(name, currentListID, dateOfCreation))
            TaskTypes.DEADLINE_CATEGORY -> listOfDeadlineCategoryLists.add(DeadlineCategoryTaskList(name, currentListID, dateOfCreation))
            TaskTypes.DEADLINE_PRIORITY_CATEGORY -> listOfDeadlinePriorityCategoryLists.add(
                DeadlinePriorityCategoryTaskList(name, currentListID, dateOfCreation)
            )
        }

        mainRepository.saveList(
            ListEntity(
                currentListID,
                name,
                dateOfCreation,
                type = when(type) {
                    TaskTypes.PRIORITY -> 1
                    TaskTypes.DEADLINE -> 2
                    TaskTypes.CATEGORY -> 3
                    TaskTypes.DEADLINE_PRIORITY -> 4
                    TaskTypes.DEADLINE_CATEGORY -> 5
                    TaskTypes.DEADLINE_PRIORITY_CATEGORY -> 6
                }
            )
        )


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
        else if(newID < 0)
            newID = 0
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
            else -> {return Status(StatusCodes.FAILURE, "error: currentList is null")
            }
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

    fun undo(): Status {
        val list = currentList
        return list?.undo() ?: Status(StatusCodes.FAILURE)
    }

    fun isStorageEmpty(): Boolean {
        val list = currentList
        return list?.isStorageEmpty() ?: true
    }


}
internal data class ListIdentifier(
    var name: String,
    var type: TaskTypes
)