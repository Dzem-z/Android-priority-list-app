package com.example.prioritylist

import com.example.prioritylist.data.*
import junit.framework.TestCase.assertTrue
import org.junit.Test

class MainPageTests {

    @Test
    fun addListsWithSameType_onAddingNewList_listAddedAndRead() {
        val page = MainPage()

        val nameID0 = "list1"
        val nameID1 = "list2"

        page.addList(TaskTypes.DEADLINE, 0, nameID0)
        page.addList(TaskTypes.DEADLINE, 1, nameID1)

        val list = page.getListByID(0)

        assertTrue(list::class == TaskTypes.DEADLINE.listType)

        assertTrue((list as DeadlineTaskList).getName() == nameID0)

    }

    @Test
    fun addAndGetLists_listsAddedAndReadCorrectly() {
        val page = MainPage()

        val nameID0 = "list1"
        val nameID1 = "list2"
        val nameID2 = "list3"
        val nameID3 = "list4"

        page.addList(TaskTypes.PRIORITY, 0, nameID0)
        page.addList(TaskTypes.DEADLINE, 1, nameID1)
        page.addList(TaskTypes.DEADLINE_CATEGORY, 2, nameID2)
        page.addList(TaskTypes.DEADLINE_PRIORITY, 3, nameID3)

        val list0 = page.getListByID(0)
        val list1 = page.getListByID(1)
        val list2 = page.getListByID(2)
        val list3 = page.getListByID(3)

        assertTrue(list0::class == TaskTypes.PRIORITY.listType)
        assertTrue(list1::class == TaskTypes.DEADLINE.listType)
        assertTrue(list2::class == TaskTypes.DEADLINE_CATEGORY.listType)
        assertTrue(list3::class == TaskTypes.DEADLINE_PRIORITY.listType)


        assertTrue((list0 as PriorityTaskList).getName() == nameID0)
        assertTrue((list1 as DeadlineTaskList).getName() == nameID1)
        assertTrue((list2 as DeadlineCategoryTaskList).getName() == nameID2)
        assertTrue((list3 as DeadlinePriorityTaskList).getName() == nameID3)

    }

    @Test
    fun addAndGetLists_correctPrioritySet() {
        val page = MainPage()

        val nameID0 = "list1"
        val nameID1 = "list2"
        val nameID2 = "list3"
        val nameID3 = "list4"

        page.addList(TaskTypes.PRIORITY, 3, nameID3)
        page.addList(TaskTypes.PRIORITY, 1, nameID1)
        page.addList(TaskTypes.PRIORITY, 2, nameID2)
        page.addList(TaskTypes.PRIORITY, 0, nameID0)


        val list0 = page.getListByID(0)
        val list1 = page.getListByID(1)
        val list2 = page.getListByID(2)
        val list3 = page.getListByID(3)


        assertTrue(list0::class == TaskTypes.PRIORITY.listType)
        assertTrue(list1::class == TaskTypes.PRIORITY.listType)
        assertTrue(list2::class == TaskTypes.PRIORITY.listType)
        assertTrue(list3::class == TaskTypes.PRIORITY.listType)

        assertTrue((list0 as PriorityTaskList).getName() == nameID0)
        assertTrue((list1 as PriorityTaskList).getName() == nameID1)
        assertTrue((list2 as PriorityTaskList).getName() == nameID2)
        assertTrue((list3 as PriorityTaskList).getName() == nameID3)
    }

    @Test
    fun initialReadingOfCurrentList_listReadingAfterAddingFirstList_listReadCorrectly() {
        val page = MainPage()

        val name = "name"

        page.addList(TaskTypes.PRIORITY, 0, name)

        assertTrue(page.currentType == TaskTypes.PRIORITY)
        assertTrue((page.currentList as PriorityTaskList).getName() == name)
        assertTrue(page.currentListID == 0)

    }

}