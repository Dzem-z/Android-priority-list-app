package com.example.prioritylist

import com.example.prioritylist.data.*
import com.example.prioritylist.domain.DataManager
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertThrows
import org.junit.Test
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

        manager.addListUseCase(0, name1, type1)

        val returnedList1 = manager.getListUseCase() as? MutableList<PriorityTask>

        manager.addListUseCase(0, name2, type2)

        val returnedList2 = manager.getListUseCase() as? MutableList<DeadlineTask>

        manager.addListUseCase(5, name3, type3)

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
            dateOfCreation = LocalDateTime.parse("2022-12-16T12:15:30"),
            deadline = LocalDateTime.parse("2024-11-16T12:15:30"),
        )

        val task2 = DeadlineTask(
            name = "test1",
            description = "desc",
            id = 0,
            dateOfCreation = LocalDateTime.parse("2022-12-12T12:15:30"),
            deadline = LocalDateTime.parse("2026-12-11T12:15:30"),
        )

        val task3 = DeadlineTask(
            name = "test1",
            description = "desc",
            id = 0,
            dateOfCreation = LocalDateTime.parse("2022-08-06T12:15:30"),
            deadline = LocalDateTime.parse("2023-02-12T12:15:30"),
        )

        manager.addListUseCase(0, name1, type1)

        manager.addTaskUseCase(task1)

        val list1 = manager.getListUseCase() as? MutableList<DeadlineTask>

        assertNotNull(list1)
        assertTrue(list1?.get(0) == task1)

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

        val name1 = "name1"; val type1 = TaskTypes.PRIORITY

        val task1 = PriorityTask(
            dateOfCreation =  LocalDateTime.parse("2023-03-12T12:15:30"),
            name = "test_name_1",
            description = "desc_1",
            id = 3,
            priority = 1
        )

        val task2 = DeadlineTask(
            name = "test1",
            description = "desc",
            id = 0,
            dateOfCreation = LocalDateTime.parse("2022-12-12T12:15:30"),
            deadline = LocalDateTime.parse("2026-12-11T12:15:30"),
        )

        manager.addListUseCase(0, name1, type1)

        manager.addTaskUseCase(task1)
        manager.addTaskUseCase(task2)

        val list = manager.getListUseCase() as? MutableList<PriorityTask>

        assertNotNull(list)

        manager.deleteTaskUseCase(list?.get(0) as Task)

        var afterDeleteList = manager.getListUseCase() as? MutableList<PriorityTask>

        assertNotNull(afterDeleteList)
        assertTrue(afterDeleteList!![0] == task1)

        manager.deleteTaskUseCase(afterDeleteList?.get(0) as Task)

        afterDeleteList = manager.getListUseCase() as? MutableList<PriorityTask>

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
            dateOfCreation = LocalDateTime.parse("2022-12-16T12:15:30"),
            deadline = LocalDateTime.parse("2024-11-16T12:15:30"),
        )

        val task2 = DeadlineTask(
            name = "test1",
            description = "desc",
            id = 0,
            dateOfCreation = LocalDateTime.parse("2022-12-12T12:15:30"),
            deadline = LocalDateTime.parse("2026-12-11T12:15:30"),
        )

        val task3 = DeadlineTask(
            name = "test1",
            description = "desc",
            id = 0,
            dateOfCreation = LocalDateTime.parse("2022-08-06T12:15:30"),
            deadline = LocalDateTime.parse("2023-02-12T12:15:30"),
        )

        manager.addListUseCase(0, name1, type1)

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

        manager.editTaskUseCase(editedTask)

        val modifiedList = manager.getListUseCase() as MutableList<DeadlineTask>

        assertTrue(modifiedList[0] == editedTask)

    }

    @Test
    fun moveToHistoryAndGetHistoryList_filePutInHistory(){
        val manager = DataManager()

        val name1 = "name1"; val type1 = TaskTypes.DEADLINE

        val task1 = DeadlineTask(
            name = "test1",
            description = "desc",
            id = 0,
            dateOfCreation = LocalDateTime.parse("2022-12-16T12:15:30"),
            deadline = LocalDateTime.parse("2024-11-16T12:15:30"),
        )

        val task2 = DeadlineTask(
            name = "test1",
            description = "desc",
            id = 0,
            dateOfCreation = LocalDateTime.parse("2022-12-12T12:15:30"),
            deadline = LocalDateTime.parse("2026-12-11T12:15:30"),
        )

        val task3 = DeadlineTask(
            name = "test1",
            description = "desc",
            id = 0,
            dateOfCreation = LocalDateTime.parse("2022-08-06T12:15:30"),
            deadline = LocalDateTime.parse("2023-02-12T12:15:30"),
        )

        manager.addListUseCase(0, name1, type1)

        manager.addTaskUseCase(task1)
        manager.addTaskUseCase(task2)
        manager.addTaskUseCase(task3)

        val list = manager.getListUseCase() as MutableList<DeadlineTask>

        manager.moveToHistoryUseCase(list[0])
        manager.moveToHistoryUseCase(list[1])
        manager.moveToHistoryUseCase(list[2])

        val newList = manager.getListUseCase() as MutableList<DeadlineTask>
        val hList = manager.getHistoryListUseCase() as MutableList<HistoryTask<DeadlineTask>>

        assertTrue(newList.isEmpty())
        assertTrue(hList.isNotEmpty())
        assertTrue(hList[2].it == list[0])
        assertTrue(hList[1].it == list[1])
        assertTrue(hList[0].it == list[1])
        assertThrows(ArrayIndexOutOfBoundsException::class.java){
            hList[3]
        }
    }

    @Test
    fun deleteFromHistory_taskDeletedFromHistory() {
        val manager = DataManager()

        val name1 = "name1"; val type1 = TaskTypes.DEADLINE

        val task1 = DeadlineTask(
            name = "test1",
            description = "desc",
            id = 0,
            dateOfCreation = LocalDateTime.parse("2022-12-16T12:15:30"),
            deadline = LocalDateTime.parse("2024-11-16T12:15:30"),
        )

        val task2 = DeadlineTask(
            name = "test1",
            description = "desc",
            id = 0,
            dateOfCreation = LocalDateTime.parse("2022-12-12T12:15:30"),
            deadline = LocalDateTime.parse("2026-12-11T12:15:30"),
        )

        val task3 = DeadlineTask(
            name = "test1",
            description = "desc",
            id = 0,
            dateOfCreation = LocalDateTime.parse("2022-08-06T12:15:30"),
            deadline = LocalDateTime.parse("2023-02-12T12:15:30"),
        )

        manager.addListUseCase(0, name1, type1)

        manager.addTaskUseCase(task1)
        manager.addTaskUseCase(task2)
        manager.addTaskUseCase(task3)

        val list = manager.getListUseCase() as MutableList<DeadlineTask>

        manager.moveToHistoryUseCase(list[0])
        manager.moveToHistoryUseCase(list[1])
        manager.moveToHistoryUseCase(list[2])

        manager.deleteFromHistoryUseCase(list[0].name)

        val historyList = manager.getHistoryListUseCase()

        assertTrue(historyList[0].it == list[1])
        assertTrue(historyList[1].it == list[2])

        manager.deleteFromHistoryUseCase(list[2].name)

        val historyList2 = manager.getHistoryListUseCase()

        assertTrue(historyList[0].it == list[1])

        assertThrows(ArrayIndexOutOfBoundsException::class.java){
            historyList[1]
        }


    }



}