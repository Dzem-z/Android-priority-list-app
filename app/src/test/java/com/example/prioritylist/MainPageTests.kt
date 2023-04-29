package com.example.prioritylist

import com.example.prioritylist.data.*
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertThrows
import org.junit.Test

class MainPageTests {

    @Test
    fun addListsWithSameType_listAddedAndRead() {
        val page = MainPage()

        val nameID0 = "list1"
        val nameID1 = "list2"

        page.addList(TaskTypes.DEADLINE, nameID0)
        page.addList(TaskTypes.DEADLINE, nameID1)

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

        page.addList(TaskTypes.PRIORITY, nameID0)
        page.addList(TaskTypes.DEADLINE, nameID1)
        page.addList(TaskTypes.DEADLINE_CATEGORY, nameID2)
        page.addList(TaskTypes.DEADLINE_PRIORITY, nameID3)

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
    fun addPrevListAndGetLists_correctIDSet() {
        val page = MainPage()

        val nameID0 = "list1"
        val nameID1 = "list2"
        val nameID2 = "list3"
        val nameID3 = "list4"

        page.addList(TaskTypes.PRIORITY, nameID3)
        page.prevList()
        page.addList(TaskTypes.PRIORITY, nameID1)
        page.addList(TaskTypes.PRIORITY, nameID2)
        page.prevList()
        page.prevList()
        page.addList(TaskTypes.PRIORITY, nameID0)


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
    fun addList_initialReadingOfCurrentList_currentListIDCurrentListCurrentTypeSetCorrectly() {
        val page = MainPage()

        val name = "name"

        page.addList(TaskTypes.PRIORITY, name)

        assertTrue(page.currentType == TaskTypes.PRIORITY)
        assertTrue((page.currentList as PriorityTaskList).getName() == name)
        assertTrue(page.currentListID == 0)

    }

    @Test
    fun addListPrevListGetList_addingCoupleOfLists_currentListIDCurrentListCurrentTypeSetCorrectly() {
        val page = MainPage()

        val name1 = "name1"; val type1 = TaskTypes.PRIORITY
        val name2 = "name2"; val type2 = TaskTypes.DEADLINE_CATEGORY
        val name3 = "name3"; val type3 = TaskTypes.DEADLINE_PRIORITY
        val name4 = "name4"; val type4 = TaskTypes.DEADLINE
        val name5 = "name5"; val type5 = TaskTypes.CATEGORY

        page.addList(type1, name1)
        page.prevList()
        page.addList(type2, name2)
        page.prevList()
        page.addList(type3, name3)
        page.prevList()
        page.addList(type4, name4)
        page.prevList()
        page.addList(type5, name5)
        page.prevList()

        val list5 = page.getListByID(0)
        val list4 = page.getListByID(1)
        val list3 = page.getListByID(2)
        val list2 = page.getListByID(3)
        val list1 = page.getListByID(4)

        assertTrue(page.currentListID == 0)
        assertNotNull(page.currentList)
        assertTrue(page.currentList!!::class  == type5.listType)

        assertTrue((list5 as TaskList<Task>).getName() == name5)
        assertTrue((list4 as TaskList<Task>).getName() == name4)
        assertTrue((list3 as TaskList<Task>).getName() == name3)
        assertTrue((list2 as TaskList<Task>).getName() == name2)
        assertTrue((list1 as TaskList<Task>).getName() == name1)

    }

    @Test
    fun addListGetList_BadIndex_ArrayIndexOutBoundsExceptionThrown() {
        val page = MainPage()

        val name1 = "name1"; val type1 = TaskTypes.PRIORITY
        val name2 = "name2"; val type2 = TaskTypes.DEADLINE_CATEGORY
        val name3 = "name3"; val type3 = TaskTypes.DEADLINE_PRIORITY
        val name4 = "name4"; val type4 = TaskTypes.DEADLINE
        val name5 = "name5"; val type5 = TaskTypes.CATEGORY

        page.addList(type1, name1)
        page.prevList()
        page.addList(type2, name2)
        page.prevList()
        page.addList(type3, name3)
        page.prevList()
        page.addList(type4, name4)
        page.prevList()
        page.addList(type5, name5)
        page.prevList()



        assertThrows(ArrayIndexOutOfBoundsException::class.java){
            page.getListByID(10)
        }
        assertThrows(ArrayIndexOutOfBoundsException::class.java){
            page.getListByID(5)
        }
        assertThrows(ArrayIndexOutOfBoundsException::class.java){
            page.getListByID(-1)
        }
    }

    @Test
    fun addListsAndTriggerNextList_NextListSetsCurrentVariablesCorrectly() {
        val page = MainPage()

        val name1 = "name1"; val type1 = TaskTypes.PRIORITY
        val name2 = "name2"; val type2 = TaskTypes.DEADLINE_CATEGORY
        val name3 = "name3"; val type3 = TaskTypes.DEADLINE_PRIORITY
        val name4 = "name4"; val type4 = TaskTypes.DEADLINE
        val name5 = "name5"; val type5 = TaskTypes.CATEGORY

        page.addList(type1, name1)
        page.prevList()
        page.addList(type2, name2)
        page.prevList()
        page.addList(type3, name3)
        page.prevList()
        page.addList(type4, name4)
        page.prevList()
        page.addList(type5, name5)
        page.prevList()

        page.nextList()

        assertTrue(page.currentListID == 1)
        assertTrue((page.currentList as TaskList<Task>).getName() == name4)
        assertTrue(page.currentType == type4)

        page.nextList()

        assertTrue(page.currentListID == 2)
        assertTrue((page.currentList as TaskList<Task>).getName() == name3)
        assertTrue(page.currentType == type3)

        page.nextList()

        assertTrue(page.currentListID == 3)
        assertTrue((page.currentList as TaskList<Task>).getName() == name2)
        assertTrue(page.currentType == type2)

        page.nextList()

        assertTrue(page.currentListID == 0)
        assertTrue((page.currentList as TaskList<Task>).getName() == name1)
        assertTrue(page.currentType == type1)

    }

    @Test
    fun addListAndNextList_CurrentVariablesSetToLastList() {
        val page = MainPage()

        val name1 = "name1"; val type1 = TaskTypes.PRIORITY
        val name2 = "name2"; val type2 = TaskTypes.DEADLINE_CATEGORY

        page.addList(type1, name1)
        page.addList(type2, name2)

        page.nextList()

        assertTrue(page.currentListID == 1)
        assertTrue((page.currentList as TaskList<Task>).getName() == name2)
        assertTrue(page.currentType == type2)
    }

    @Test
    fun addListAndPrevList_CurrentVariablesSetToFirstList() {
        val page = MainPage()

        val name1 = "name1"; val type1 = TaskTypes.PRIORITY
        val name2 = "name2"; val type2 = TaskTypes.DEADLINE_CATEGORY

        page.addList(type1, name1)
        page.addList(type2, name2)

        page.prevList()
        page.prevList()
        page.prevList()

        assertTrue(page.currentListID == 0)
        assertTrue((page.currentList as TaskList<Task>).getName() == name1)
        assertTrue(page.currentType == type1)
    }

    @Test
    fun deleteCurrentListInTheMiddle_onDelete_listDeletedAndPreviousListSetAsCurrent() {
        val page = MainPage()

        val name1 = "name1"; val type1 = TaskTypes.PRIORITY
        val name2 = "name2"; val type2 = TaskTypes.DEADLINE_CATEGORY
        val name3 = "name3"; val type3 = TaskTypes.DEADLINE_PRIORITY
        val name4 = "name4"; val type4 = TaskTypes.DEADLINE

        page.addList(type1, name1)
        page.addList(type2, name2)
        page.addList(type3, name3)
        page.addList(type4, name4)
        page.prevList()

        page.deleteCurrentList()

        assertTrue(page.currentListID == 1)
        assertTrue(page.currentType == TaskTypes.DEADLINE_CATEGORY)
        assertTrue((page.currentList as DeadlineCategoryTaskList).getName() == name2)
    }

    @Test
    fun deleteCurrentListAtTheEnd_onDelete_listDeletedAndPreviousListSetAsCurrent() {
        val page = MainPage()

        val name1 = "name1"; val type1 = TaskTypes.PRIORITY
        val name2 = "name2"; val type2 = TaskTypes.DEADLINE_CATEGORY
        val name3 = "name3"; val type3 = TaskTypes.DEADLINE_PRIORITY
        val name4 = "name4"; val type4 = TaskTypes.DEADLINE

        page.addList(type1, name1)
        page.addList(type2, name2)
        page.addList(type3, name3)
        page.addList(type4, name4)

        page.deleteCurrentList()

        assertTrue(page.currentListID == 3)
        assertTrue(page.currentType == TaskTypes.DEADLINE_PRIORITY)
        assertTrue((page.currentList as DeadlineCategoryTaskList).getName() == name3)
    }

    @Test
    fun deleteCurrentListAtTheStart_onDelete_listDeletedAndPreviousListSetAsCurrent() {
        val page = MainPage()

        val name1 = "name1"; val type1 = TaskTypes.PRIORITY
        val name2 = "name2"; val type2 = TaskTypes.DEADLINE_CATEGORY
        val name3 = "name3"; val type3 = TaskTypes.DEADLINE_PRIORITY
        val name4 = "name4"; val type4 = TaskTypes.DEADLINE

        page.addList(type1, name1)
        page.addList(type2, name2)
        page.addList(type3, name3)
        page.addList(type4, name4)
        page.prevList()
        page.prevList()
        page.prevList()

        page.deleteCurrentList()

        assertTrue(page.currentListID == 0)
        assertTrue(page.currentType == TaskTypes.DEADLINE_CATEGORY)
        assertTrue((page.currentList as DeadlineCategoryTaskList).getName() == name2)
    }

    @Test
    fun changeIDofCurrentList_onListOrderChange_listIDAndOrderChanged() {
        val page = MainPage()

        val name1 = "name1"; val type1 = TaskTypes.PRIORITY
        val name2 = "name2"; val type2 = TaskTypes.DEADLINE_CATEGORY
        val name3 = "name3"; val type3 = TaskTypes.DEADLINE_PRIORITY
        val name4 = "name4"; val type4 = TaskTypes.DEADLINE

        page.addList(type1, name1)
        page.addList(type2, name2)
        page.addList(type3, name3)
        page.addList(type4, name4)

        page.changeIDofCurrentList(0)
        page.nextList()

        assertTrue(page.currentListID == 1)
        assertTrue(page.currentType == type2)
        assertTrue((page.currentList as DeadlineCategoryTaskList).getName() == name2)


        page.changeIDofCurrentList(2)
        page.prevList()

        assertTrue(page.currentListID == 1)
        assertTrue(page.currentType == type3)
        assertTrue((page.currentList as DeadlinePriorityTaskList).getName() == name3)


    }
}