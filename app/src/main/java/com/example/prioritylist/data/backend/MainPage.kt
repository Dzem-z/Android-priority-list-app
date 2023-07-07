package com.example.prioritylist.data.backend

import androidx.annotation.VisibleForTesting
import androidx.compose.ui.graphics.Color
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

    private suspend fun shift(startingID: Int, value: Int){
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
        val listHolder: List<ListEntity> = mainRepository.loadListCredentials()

        for(i in listHolder)
            listIdentifiers.add(ListIdentifier("<init>", TaskTypes.PRIORITY))

        for(i in listHolder) {
            listIdentifiers.removeAt(i.listID)
            listIdentifiers.add(i.listID, ListIdentifier(i.name, i.type))

            when(i.type) {
                TaskTypes.PRIORITY -> listOfPriorityLists.add(PriorityTaskList(i.name, i.listID, i.dateOfCreation, listRepository, mainRepository.loadListByID(i.listID).map {
                    PriorityTask(priority = it.priority!!, dateOfCreation = it.dateOfCreation, description = it.description, name = it.name )
                }.toMutableList()))
                TaskTypes.DEADLINE -> listOfDeadlineLists.add(DeadlineTaskList(i.name, i.listID, i.dateOfCreation, listRepository, mainRepository.loadListByID(i.listID).map {
                    DeadlineTask(deadline = it.deadline!!, dateOfCreation = it.dateOfCreation, description = it.description, name = it.name)
                }.toMutableList() ))
                TaskTypes.CATEGORY -> listOfCategoryLists.add(CategoryTaskList(i.name, i.listID, i.dateOfCreation, listRepository))
                TaskTypes.DEADLINE_PRIORITY -> listOfDeadlinePriorityLists.add(DeadlinePriorityTaskList(i.name, i.listID, i.dateOfCreation, listRepository))
                TaskTypes.DEADLINE_CATEGORY -> listOfDeadlineCategoryLists.add(DeadlineCategoryTaskList(i.name, i.listID, i.dateOfCreation, listRepository))
                TaskTypes.DEADLINE_PRIORITY_CATEGORY -> listOfDeadlinePriorityCategoryLists.add(DeadlinePriorityCategoryTaskList(i.name, i.listID, i.dateOfCreation, listRepository))
            }
        }

        if (size() > 0) {
            currentListID = 0
            currentList = getListByID(0)
            currentType = listIdentifiers[0].type
        }


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
            TaskTypes.DEADLINE -> listOfDeadlineLists.add(DeadlineTaskList(name, currentListID, dateOfCreation, listRepository))
            TaskTypes.PRIORITY -> listOfPriorityLists.add(PriorityTaskList(name, currentListID, dateOfCreation, listRepository))
            TaskTypes.CATEGORY -> listOfCategoryLists.add(CategoryTaskList(name, currentListID, dateOfCreation, listRepository))
            TaskTypes.DEADLINE_PRIORITY -> listOfDeadlinePriorityLists.add(DeadlinePriorityTaskList(name, currentListID, dateOfCreation, listRepository))
            TaskTypes.DEADLINE_CATEGORY -> listOfDeadlineCategoryLists.add(DeadlineCategoryTaskList(name, currentListID, dateOfCreation, listRepository))
            TaskTypes.DEADLINE_PRIORITY_CATEGORY -> listOfDeadlinePriorityCategoryLists.add(
                DeadlinePriorityCategoryTaskList(name, currentListID, dateOfCreation, listRepository)
            )
        }

        mainRepository.shift(currentListID, 1, 100_000)
        listRepository.shift(currentListID, 1, 100_000)

        mainRepository.saveList(
            ListEntity(
                currentListID,
                name,
                dateOfCreation,
                type = type
            )
        )


        currentList = getListByID(currentListID)
        currentType = listIdentifiers[currentListID].type

        return Status(StatusCodes.SUCCESS)
    }

    fun size(): Int {
        return listOfPriorityLists.size + listOfCategoryLists.size + listOfDeadlineLists.size + listOfDeadlineCategoryLists.size + listOfDeadlinePriorityLists.size + listOfDeadlinePriorityCategoryLists.size
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

    suspend fun changeIDofCurrentList(newId: Int): Status {
        var newID = newId
        val list = getListByID(currentListID)

        if (newID > listIdentifiers.size - 1)
            newID = listIdentifiers.size - 1
        else if(newID < 0)
            newID = 0
        return if (list != null) {
            mainRepository.changeIdOfCurrent(currentListID, newID)
            listRepository.changeIdOfCurrent(currentListID, newID)
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

    suspend fun deleteCurrentList(): Status {
        mainRepository.deleteList(currentListID)
        mainRepository.shift(currentListID, -1, 100_000)
        listRepository.shift(currentListID, -1, 100_000)
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

    suspend fun undo(): Status {
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