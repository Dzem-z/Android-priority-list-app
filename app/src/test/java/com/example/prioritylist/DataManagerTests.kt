package com.example.prioritylist

import com.example.prioritylist.data.*
import com.example.prioritylist.domain.DataManager
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertThrows
import org.junit.Test
import java.text.SimpleDateFormat
import java.time.LocalDateTime



class DataManagerTests {

    @Test
    fun getListUseCase_onEmptyGet_emptyListReturned() {
        val manager = DataManager()

        val returnedList = manager.getListUseCase()

        assertTrue(returnedList.isEmpty())
    }

    @Test
    fun addListUseCase_onAddingLists_correctListReturned() {
        val manager = DataManager()

        val name1 = "name1"; val type1 = TaskTypes.PRIORITY
        val name2 = "name2"; val type2 = TaskTypes.DEADLINE
        val name3 = "name3"; val type3 = TaskTypes.DEADLINE_PRIORITY

        manager.addListUseCase(0, name1, type1, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"))

        val returnedList1 = manager.getListUseCase() as? MutableList<PriorityTask>

        manager.addListUseCase(0, name2, type2, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"))

        val returnedList2 = manager.getListUseCase() as? MutableList<DeadlineTask>

        manager.addListUseCase(5, name3, type3, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"))

        val returnedList3 = manager.getListUseCase() as? MutableList<DeadlinePriorityTask>

        assertNotNull(returnedList1)
        assertNotNull(returnedList2)
        assertNotNull(returnedList3)
    }

    @Test
    fun addTaskAndAddList_TaskAddedToTheList() {
        val manager = DataManager()

        val name1 = "name1"; val type1 = TaskTypes.DEADLINE

        val task1 = DeadlineTask(
            name = "test1",
            description = "desc",
            id = 0,
            dateOfCreation = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"),
            deadline = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2024-11-1612:15:30"),
        )

        val task2 = DeadlineTask(
            name = "test2",
            description = "desc",
            id = 0,
            dateOfCreation = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1212:15:30"),
            deadline = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-12-1112:15:30"),
        )

        val task3 = DeadlineTask(
            name = "test1",
            description = "desc",
            id = 0,
            dateOfCreation = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-08-0612:15:30"),
            deadline = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-02-1212:15:30"),
        )

        manager.addListUseCase(0, name1, type1, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"))

        manager.addTaskUseCase(task1)

        val list1 = manager.getListUseCase() as? MutableList<DeadlineTask>

        assertNotNull(list1)
        val retTask = list1?.get(0)
        assertTrue(retTask == task1)

        manager.addTaskUseCase(task2)

        val list2 = manager.getListUseCase() as? MutableList<DeadlineTask>

        assertNotNull(list2)
        assertTrue(list2?.get(0) == task2)

        manager.addTaskUseCase(task3)

        val list3 = manager.getListUseCase() as? MutableList<DeadlineTask>

        assertNotNull(list3)
        assertTrue(list3?.get(0) == task3)


    }

    @Test
    fun deleteTaskAndAddTaskAndAddList_TaskDeletedFromTheList() {
        val manager = DataManager()

        val name1 = "name1"; val type1 = TaskTypes.DEADLINE

        val task1 = DeadlineTask(
            name = "test1",
            description = "desc",
            id = 0,
            dateOfCreation = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"),
            deadline = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2024-11-1612:15:30"),
        )

        val task2 = DeadlineTask(
            name = "test2",
            description = "desc",
            id = 0,
            dateOfCreation = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1212:15:30"),
            deadline = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-12-1112:15:30"),
        )

        manager.addListUseCase(0, name1, type1, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"))

        manager.addTaskUseCase(task1)
        manager.addTaskUseCase(task2)

        val list = manager.getListUseCase() as? MutableList<DeadlineTask>

        assertNotNull(list)

        manager.deleteTaskUseCase(list!!.get(0))

        var afterDeleteList = manager.getListUseCase() as? MutableList<DeadlineTask>

        assertNotNull(afterDeleteList)
        assertTrue(afterDeleteList!![0] == task1)

        manager.deleteTaskUseCase(afterDeleteList!!.get(0))

        afterDeleteList = manager.getListUseCase() as? MutableList<DeadlineTask>

        assertNotNull(afterDeleteList)
        assertTrue(afterDeleteList!!.isEmpty())

    }

    @Test
    fun editTaskAndAddTaskAndAddList_TaskEditedProperly() {
        val manager = DataManager()

        val name1 = "name1"; val type1 = TaskTypes.DEADLINE

        val task1 = DeadlineTask(
            name = "test1",
            description = "desc",
            id = 0,
            dateOfCreation = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"),
            deadline = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2024-11-1612:15:30"),
        )

        val task2 = DeadlineTask(
            name = "test2",
            description = "desc",
            id = 0,
            dateOfCreation = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1212:15:30"),
            deadline = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2026-12-1112:15:30"),
        )

        val task3 = DeadlineTask(
            name = "test3",
            description = "desc",
            id = 0,
            dateOfCreation = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-08-0612:15:30"),
            deadline = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-02-1212:15:30"),
        )

        manager.addListUseCase(0, name1, type1, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"))

        manager.addTaskUseCase(task1)
        manager.addTaskUseCase(task2)
        manager.addTaskUseCase(task3)

        val list = manager.getListUseCase() as MutableList<DeadlineTask>

        val editedTask = DeadlineTask(
            name = "edited task",
            description = "edited description",
            id = list[0].id,
            dateOfCreation = list[0].dateOfCreation,
            deadline = list[0].deadline
        )

        manager.editTaskUseCase(list[0].id, editedTask)

        val modifiedList = manager.getListUseCase() as MutableList<DeadlineTask>

        assertTrue(modifiedList[0] == editedTask)

    }

    @Test
    fun editTask_TaskModifiedWithoutChangingItsName_TaskProperlyEdited(){
        val manager = DataManager()

        val task1 = PriorityTask(
            dateOfCreation =   SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1212:15:30"),
            name = "test_name_1",
            description = "desc_1",
            id = 3,
            priority = 12
        )
        val task2 = PriorityTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-2412:15:30"),
            name = "test_name_2",
            description = "desc_2",
            id = 3,
            priority = 11
        )
        val task3 = PriorityTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1212:15:30"),
            name = "test_name_3",
            description = "desc_1",
            id = 3,
            priority = 10
        )
        val task4 = PriorityTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1212:15:30"),
            name = "test_name_4",
            description = "desc_1",
            id = 3,
            priority = 9
        )
        val task5 = PriorityTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-2412:15:30"),
            name = "test_name_5",
            description = "desc_2",
            id = 3,
            priority = 8
        )
        val task6 = PriorityTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1212:15:30"),
            name = "test_name_6",
            description = "desc_1",
            id = 3,
            priority = 7
        )
        val task7 = PriorityTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1212:15:30"),
            name = "test_name_7",
            description = "desc_1",
            id = 3,
            priority = 6
        )
        val task8 = PriorityTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-2412:15:30"),
            name = "test_name_8",
            description = "desc_2",
            id = 3,
            priority = 5
        )
        val task9 = PriorityTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1212:15:30"),
            name = "test_name_9",
            description = "desc_1",
            id = 3,
            priority = 4
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

        manager.addListUseCase(0, "test", TaskTypes.PRIORITY, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"))

        manager.addTaskUseCase(task1)
        manager.addTaskUseCase(task2)
        manager.addTaskUseCase(task3)
        manager.addTaskUseCase(task4)
        manager.addTaskUseCase(task5)
        manager.addTaskUseCase(task6)
        manager.addTaskUseCase(task7)
        manager.addTaskUseCase(task8)
        manager.addTaskUseCase(task9)

        manager.addListUseCase(1, "test deadline", TaskTypes.DEADLINE, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"))
        manager.addTaskUseCase(deadlineTask1)
        manager.addTaskUseCase(deadlineTask2)
        manager.addTaskUseCase(deadlineTask3)

        manager.prevListUseCase()

        val oldList = manager.getListUseCase() as MutableList<PriorityTask>

        val newTask = PriorityTask(id = oldList[0].id, name = oldList[0].name, description = oldList[0].description, priority = oldList[0].priority, dateOfCreation = oldList[0].dateOfCreation)

        manager.editTaskUseCase(newTask.id, newTask)

        val list = manager.getListUseCase()



        assertTrue(list[0] == oldList[0])
        assertTrue(list[1] == oldList[1])
        assertTrue(list[2] == oldList[2])
        assertTrue(list[3] == oldList[3])
        assertTrue(list[4] == oldList[4])
        assertTrue(list[5] == oldList[5])
        assertTrue(list[6] == oldList[6])
        assertTrue(list[7] == oldList[7])
        assertTrue(list[8] == oldList[8])


    }

    @Test
    fun moveToHistoryAndGetHistoryList_filePutInHistory(){
        val manager = DataManager()

        val name1 = "name1"; val type1 = TaskTypes.DEADLINE

        val task1 = DeadlineTask(
            name = "test1",
            description = "desc",
            id = 0,
            dateOfCreation = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"),
            deadline = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2024-11-1612:15:30"),
        )

        val task2 = DeadlineTask(
            name = "test2",
            description = "desc",
            id = 0,
            dateOfCreation = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1212:15:30"),
            deadline = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2026-12-1112:15:30"),
        )

        val task3 = DeadlineTask(
            name = "test3",
            description = "desc",
            id = 0,
            dateOfCreation = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-08-0612:15:30"),
            deadline = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-02-1212:15:30"),
        )

        manager.addListUseCase(0, name1, type1, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"))

        manager.addTaskUseCase(task1)
        manager.addTaskUseCase(task2)
        manager.addTaskUseCase(task3)

        val list = manager.getListUseCase() as MutableList<DeadlineTask>

        manager.moveToHistoryUseCase(list[0], SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2030-08-0612:15:30"))
        manager.moveToHistoryUseCase(list[1], SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2030-09-0612:15:30"))
        manager.moveToHistoryUseCase(list[2], SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2030-10-0612:15:30"))

        val newList = manager.getListUseCase() as MutableList<DeadlineTask>
        val hList = manager.getHistoryListUseCase() as MutableList<HistoryTask<DeadlineTask>>

        assertTrue(newList.isEmpty())
        assertTrue(hList.isNotEmpty())
        assertTrue(hList[2].it == list[0])
        assertTrue(hList[1].it == list[1])
        assertTrue(hList[0].it == list[1])
        assertTrue(hList.size == 3)
    }

    @Test
    fun deleteFromHistory_taskDeletedFromHistory() {
        val manager = DataManager()

        val name1 = "name1"; val type1 = TaskTypes.DEADLINE

        val task1 = DeadlineTask(
            name = "test1",
            description = "desc",
            id = 0,
            dateOfCreation = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"),
            deadline = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2024-11-1612:15:30"),
        )

        val task2 = DeadlineTask(
            name = "test2",
            description = "desc",
            id = 0,
            dateOfCreation = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1212:15:30"),
            deadline = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2026-12-1112:15:30"),
        )

        val task3 = DeadlineTask(
            name = "test3",
            description = "desc",
            id = 0,
            dateOfCreation = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-08-0612:15:30"),
            deadline = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-02-1212:15:30"),
        )

        manager.addListUseCase(0, name1, type1, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"))

        manager.addTaskUseCase(task1)
        manager.addTaskUseCase(task2)
        manager.addTaskUseCase(task3)

        val list = manager.getListUseCase() as MutableList<DeadlineTask>

        manager.moveToHistoryUseCase(list[0], SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2030-08-0612:15:30"))
        manager.moveToHistoryUseCase(list[1], SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2030-09-0612:15:30"))
        manager.moveToHistoryUseCase(list[2], SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2030-10-0612:15:30"))

        manager.deleteFromHistoryUseCase(list[0].name)

        val historyList = manager.getHistoryListUseCase()

        assertTrue(historyList[0].it == list[1])
        assertTrue(historyList[1].it == list[2])

        manager.deleteFromHistoryUseCase(list[2].name)

        val historyList2 = manager.getHistoryListUseCase()

        assertTrue(historyList[0].it == list[1])
        assertTrue(historyList.size == 1)
    }

    @Test
    fun deleteUntil_allTasksDeletedUntil() {
        val manager = DataManager()

        val name1 = "name1"; val type1 = TaskTypes.DEADLINE

        val task1 = DeadlineTask(
            name = "test1",
            description = "desc",
            id = 0,
            dateOfCreation = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"),
            deadline = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2021-02-1612:15:30"),
        )

        val task2 = DeadlineTask(
            name = "test2",
            description = "desc",
            id = 0,
            dateOfCreation = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1212:15:30"),
            deadline = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2021-02-2012:15:30"),
        )

        val task3 = DeadlineTask(
            name = "test3",
            description = "desc",
            id = 0,
            dateOfCreation = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-08-0612:15:30"),
            deadline = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2021-06-1212:15:30"),
        )

        val task4 = DeadlineTask(
            name = "test4",
            description = "desc",
            id = 0,
            dateOfCreation = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-08-0612:15:30"),
            deadline = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2021-07-1212:15:30"),
        )

        val task5 = DeadlineTask(
            name = "test5",
            description = "desc",
            id = 0,
            dateOfCreation = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-08-0612:15:30"),
            deadline = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2021-09-1212:15:30"),
        )

        manager.addListUseCase(0, name1, type1, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"))

        manager.addTaskUseCase(task1)
        manager.addTaskUseCase(task2)
        manager.addTaskUseCase(task3)
        manager.addTaskUseCase(task4)
        manager.addTaskUseCase(task5)

        manager.moveToHistoryUseCase(task5, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"))
        manager.moveToHistoryUseCase(task4, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-01-1612:15:30"))
        manager.moveToHistoryUseCase(task3, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-02-1612:15:30"))
        manager.moveToHistoryUseCase(task2, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1612:15:30"))
        manager.moveToHistoryUseCase(task1, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-04-1612:15:30"))

        val historyList = manager.getHistoryListUseCase()

        manager.deleteUntilUseCase(historyList[2].completionDate)

        val afterRemovalList = manager.getHistoryListUseCase()

        assertTrue(afterRemovalList[0] == historyList[2])
        assertTrue(afterRemovalList[1] == historyList[3])
        assertTrue(afterRemovalList[2] == historyList[4])
        assertTrue(afterRemovalList.size == 3)
    }

    @Test
    fun changeName_nameChanged() {
        val manager = DataManager()

        val name1 = "name1"; val type1 = TaskTypes.DEADLINE
        val newName = "new"

        manager.addListUseCase(0, name1, type1, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"))

        manager.changeNameUseCase(newName)

        assertTrue(manager.getNameUseCase() == newName)
    }

    @Test
    fun undo_undoAction_previousStateRestored() {
        val manager = DataManager()

        val name1 = "name1"; val type1 = TaskTypes.DEADLINE

        val task1 = DeadlineTask(
            name = "test1",
            description = "desc",
            id = 0,
            dateOfCreation = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"),
            deadline = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2024-11-1612:15:30"),
        )

        val task2 = DeadlineTask(
            name = "test2",
            description = "desc",
            id = 0,
            dateOfCreation = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1212:15:30"),
            deadline = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2026-12-1112:15:30"),
        )

        val task3 = DeadlineTask(
            name = "test3",
            description = "desc",
            id = 0,
            dateOfCreation = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-08-0612:15:30"),
            deadline = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-02-1212:15:30"),
        )

        manager.addListUseCase(0, name1, type1, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"))

        manager.addTaskUseCase(task1)
        manager.addTaskUseCase(task2)
        manager.addTaskUseCase(task3)

        val list1 = manager.getListUseCase() as MutableList<DeadlineTask>

        manager.deleteTaskUseCase(list1[0])

        val editedTask = DeadlineTask(
            name = "new",
            description = "welcome",
            id = list1[1].id,
            dateOfCreation = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-08-0612:15:30"),
            deadline = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-02-1212:15:30"),
        )

        manager.editTaskUseCase(list1[1].id, editedTask)

        manager.undoUseCase()

        val list2 = manager.getListUseCase() as MutableList<DeadlineTask>

        assertTrue(list2[0] == list1[1])

        manager.undoUseCase()

        val list3 = manager.getListUseCase() as MutableList<DeadlineTask>

        assertTrue(list3.size == 3)
        assertTrue(list3[0] == list1[0])

    }

    @Test
    fun prevList_prevListSetAsCurrent() {
        val manager = DataManager()

        val name1 = "name1"; val type1 = TaskTypes.PRIORITY
        val name2 = "name2"; val type2 = TaskTypes.DEADLINE
        val name3 = "name3"; val type3 = TaskTypes.DEADLINE_PRIORITY

        manager.addListUseCase(0, name1, type1, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"))
        manager.addListUseCase(1, name2, type2, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"))
        manager.addListUseCase(2, name3, type3, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"))

        assertTrue(manager.prevListUseCase() == TaskTypes.DEADLINE)

        val newName1 = manager.getNameUseCase()

        assertTrue(newName1 == name2)

        assertTrue(manager.prevListUseCase() == TaskTypes.PRIORITY)

        val newName2 = manager.getNameUseCase()

        assertTrue(newName2 == name1)
    }



    @Test
    fun changeIDprevList_IDChanged(){
        val manager = DataManager()

        val name1 = "name1"; val type1 = TaskTypes.PRIORITY
        val name2 = "name2"; val type2 = TaskTypes.DEADLINE
        val name3 = "name3"; val type3 = TaskTypes.DEADLINE_PRIORITY

        val newID = 5

        manager.addListUseCase(0, name2, type2, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"))
        manager.addListUseCase(0, name1, type1, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"))
        manager.addListUseCase(5, name3, type3, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"))

        manager.prevListUseCase()
        manager.prevListUseCase()

        manager.changeIDUseCase(newID)

        manager.prevListUseCase()

        val newName1 = manager.getNameUseCase()

        assertTrue(newName1 == name3)

        manager.prevListUseCase()

        val newName2 = manager.getNameUseCase()
        assertTrue(newName2 == name2)

    }

    @Test
    fun nextList_nextListSetAsCurrent() {
        val manager = DataManager()

        val name1 = "name1"; val type1 = TaskTypes.PRIORITY
        val name2 = "name2"; val type2 = TaskTypes.DEADLINE
        val name3 = "name3"; val type3 = TaskTypes.DEADLINE_PRIORITY

        manager.addListUseCase(0, name3, type3, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"))
        manager.addListUseCase(0, name2, type2, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"))
        manager.addListUseCase(0, name1, type1, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"))

        assertTrue(manager.nextListUseCase() == TaskTypes.DEADLINE)

        val newName1 = manager.getNameUseCase()

        assertTrue(newName1 == name2)

        assertTrue(manager.nextListUseCase() == TaskTypes.DEADLINE_PRIORITY)

        val newName2 = manager.getNameUseCase()

        assertTrue(newName2 == name3)
    }

    @Test
    fun deleteCurrentList_listRemovedAndCurrentSetToPrevious() {
        val manager = DataManager()

        val name1 = "name1"; val type1 = TaskTypes.PRIORITY
        val name2 = "name2"; val type2 = TaskTypes.DEADLINE
        val name3 = "name3"; val type3 = TaskTypes.DEADLINE_PRIORITY

        manager.addListUseCase(0, name3, type3, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"))
        manager.addListUseCase(0, name2, type2, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"))
        manager.addListUseCase(0, name1, type1, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"))

        manager.deleteCurrentListUseCase()

        val newName = manager.getNameUseCase()

        assertTrue(newName == name2)

        manager.deleteCurrentListUseCase()

        val newName1 = manager.getNameUseCase()

        assertTrue(newName1 == name3)

        manager.deleteCurrentListUseCase()

        assertThrows(NullPointerException::class.java){
            manager.getNameUseCase()
        }

    }

}
