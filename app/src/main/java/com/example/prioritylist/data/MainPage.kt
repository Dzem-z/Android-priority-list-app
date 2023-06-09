package com.example.prioritylist.data

import androidx.annotation.VisibleForTesting
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Calendar
import java.util.Date

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


    //testing
    init{
        //-------------------------testing values

        val task1 = PriorityTask(
            dateOfCreation =   SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1212:15:30"),
            name = "test_name_1",
            description = "desc_1",
            id = 3,
            priority = 3
        )
        val task2 = PriorityTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-2412:15:30"),
            name = "test_name_2",
            description = "desc_2",
            id = 3,
            priority = 2
        )
        val task3 = PriorityTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1212:15:30"),
            name = "test_name_3",
            description = "desc_1",
            id = 3,
            priority = 1
        )
        val task4 = PriorityTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1212:15:30"),
            name = "test_name_4",
            description = "desc_1",
            id = 3,
            priority = 12
        )
        val task5 = PriorityTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-2412:15:30"),
            name = "test_name_5",
            description = "desc_2",
            id = 3,
            priority = 2
        )
        val task6 = PriorityTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1212:15:30"),
            name = "test_name_6",
            description = "desc_1",
            id = 3,
            priority = 6
        )
        val task7 = PriorityTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1212:15:30"),
            name = "test_name_7",
            description = "desc_1",
            id = 3,
            priority = 3
        )
        val task8 = PriorityTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-2412:15:30"),
            name = "test_name_8",
            description = "desc_2",
            id = 3,
            priority = 2
        )
        val task9 = PriorityTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1212:15:30"),
            name = "test_name_9",
            description = "desc_1",
            id = 3,
            priority = 1
        )

        val deadlineTask1 = DeadlineTask(
            dateOfCreation = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-01-1212:15:30"),
            name = "test_name_1",
            description = "desc_1",
            id = 3,
            deadline = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-04-1212:15:30")
        )
        val deadlineTask2 = DeadlineTask(
            dateOfCreation = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-02-1212:15:30"),
            name = "test_name_2",
            description = "desc_2",
            id = 3,
            deadline = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-05-1212:15:30")
        )
        val deadlineTask3 = DeadlineTask(
            dateOfCreation = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1212:15:30"),
            name = "test_name_3",
            description = "desc_3",
            id = 2,
            deadline = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-06-1212:15:30")
        )

        addList(TaskTypes.PRIORITY, "test", Calendar.getInstance().time)
        val list = currentList
        (list as PriorityTaskList).add(task1)
        (list as PriorityTaskList).add(task2)
        (list as PriorityTaskList).add(task3)
        (list as PriorityTaskList).add(task4)
        (list as PriorityTaskList).add(task5)
        (list as PriorityTaskList).add(task6)
        (list as PriorityTaskList).add(task7)
        (list as PriorityTaskList).add(task8)
        (list as PriorityTaskList).add(task9)
        list.history.pushTask(task8, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2025-04-1212:15:30"))

        addList(TaskTypes.DEADLINE, "test deadline", Calendar.getInstance().time)
        val listDeadline = currentList
        (listDeadline as DeadlineTaskList).history.pushTask(deadlineTask1, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2025-04-1212:15:30"))
        (listDeadline as DeadlineTaskList).history.pushTask(deadlineTask2, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2025-04-1212:15:30"))
        (listDeadline as DeadlineTaskList).history.pushTask(deadlineTask3, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2025-04-1212:15:30"))
        prevList()
        //testing
    }


    fun addList(
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
            TaskTypes.DEADLINE_PRIORITY_CATEGORY -> listOfDeadlinePriorityCategoryLists.add(DeadlinePriorityCategoryTaskList(name, currentListID, dateOfCreation))
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

    fun undo(): Status {
        TODO("not yet implemented")
    }


}

internal data class ListIdentifier(
    var name: String,
    var type: TaskTypes
)