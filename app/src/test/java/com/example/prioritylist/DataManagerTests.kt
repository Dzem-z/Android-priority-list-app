package com.example.prioritylist

import com.example.prioritylist.data.*
import com.example.prioritylist.domain.DataManager
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
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

        val name1 = "name1"; val type1 = TaskTypes.PRIORITY

        val task1 = PriorityTask(
            dateOfCreation =  LocalDateTime.parse("2023-03-12T12:15:30"),
            name = "test_name_1",
            description = "desc_1",
            id = 3,
            priority = 1
        )

        manager.addListUseCase(0, name1, type1)

        manager.addTaskUseCase(task1)

        val list = manager.getListUseCase() as? MutableList<PriorityTask>

        assertNotNull(list)
        assertTrue(list?.get(0) == task1)
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

        manager.addListUseCase(0, name1, type1)

        manager.addTaskUseCase(task1)

        val list = manager.getListUseCase() as? MutableList<PriorityTask>

        assertNotNull(list)

        manager.deleteTaskUseCase(list?.get(0) as Task)

        val afterDeleteList = manager.getListUseCase() as? MutableList<PriorityTask>

        assertNotNull(list)
        assertTrue(afterDeleteList!!.isEmpty())

    }

}