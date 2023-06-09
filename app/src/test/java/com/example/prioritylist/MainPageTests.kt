package com.example.prioritylist

import com.example.prioritylist.data.backend.*
import com.example.prioritylist.data.backend.MainPage
import com.example.prioritylist.data.backend.TaskTypes
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertThrows
import org.junit.Test
import java.text.SimpleDateFormat

class MainPageTests {

    @Test
    fun addListsWithSameType_listAddedAndRead() {
        val page = MainPage(listRepository = ListRepositoryMock(), mainRepository = MainRepositoryMock())

        val nameID0 = "list1"
        val nameID1 = "list2"

        runBlocking {
            page.addList(
                TaskTypes.DEADLINE,
                nameID0,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
            page.addList(
                TaskTypes.DEADLINE,
                nameID1,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
        }

        val list = page.getListByID(0)
        assertNotNull(list)

        assertTrue(list!!::class == TaskTypes.DEADLINE.listType)

        assertTrue((list as DeadlineTaskList).getName() == nameID0)

    }

    @Test
    fun addAndGetLists_listsAddedAndReadCorrectly() {
        val page = MainPage(listRepository = ListRepositoryMock(), mainRepository = MainRepositoryMock())

        val nameID0 = "list1"
        val nameID1 = "list2"
        val nameID2 = "list3"
        val nameID3 = "list4"

        runBlocking {
            page.addList(
                TaskTypes.PRIORITY,
                nameID0,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
            page.addList(
                TaskTypes.DEADLINE,
                nameID1,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
            page.addList(
                TaskTypes.DEADLINE_CATEGORY,
                nameID2,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
            page.addList(
                TaskTypes.DEADLINE_PRIORITY,
                nameID3,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
        }

        val list0 = page.getListByID(0)
        val list1 = page.getListByID(1)
        val list2 = page.getListByID(2)
        val list3 = page.getListByID(3)

        assertTrue(list0!!::class == TaskTypes.PRIORITY.listType)
        assertTrue(list1!!::class == TaskTypes.DEADLINE.listType)
        assertTrue(list2!!::class == TaskTypes.DEADLINE_CATEGORY.listType)
        assertTrue(list3!!::class == TaskTypes.DEADLINE_PRIORITY.listType)


        assertTrue((list0 as PriorityTaskList).getName() == nameID0)
        assertTrue((list1 as DeadlineTaskList).getName() == nameID1)
        assertTrue((list2 as DeadlineCategoryTaskList).getName() == nameID2)
        assertTrue((list3 as DeadlinePriorityTaskList).getName() == nameID3)

    }

    @Test
    fun changeID_idChangedAndOthersShifted() {
        val page = MainPage(listRepository = ListRepositoryMock(), mainRepository = MainRepositoryMock())

        val nameID0 = "list1"
        val nameID1 = "list2"
        val nameID2 = "list3"
        val nameID3 = "list4"
        val nameID4 = "list5"

        runBlocking {
            page.addList(
                TaskTypes.PRIORITY,
                nameID4,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
            page.addList(
                TaskTypes.PRIORITY,
                nameID3,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
            page.addList(
                TaskTypes.PRIORITY,
                nameID2,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
            page.addList(
                TaskTypes.PRIORITY,
                nameID1,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
            page.addList(
                TaskTypes.PRIORITY,
                nameID0,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )

            page.changeIDofCurrentList(0)
            page.nextList()
            page.changeIDofCurrentList(4)
            page.prevList()
            page.changeIDofCurrentList(1)
            page.nextList()
            page.changeIDofCurrentList(3)
        }

        val list0 = page.getListByID(0) as? PriorityTaskList
        val list1 = page.getListByID(1) as? PriorityTaskList
        val list2 = page.getListByID(2) as? PriorityTaskList
        val list3 = page.getListByID(3) as? PriorityTaskList
        val list4 = page.getListByID(4) as? PriorityTaskList

        assertNotNull(list0)
        assertNotNull(list1)
        assertNotNull(list2)
        assertNotNull(list3)
        assertNotNull(list4)

        assertTrue(list0!!.getName() == nameID0)
        assertTrue(list1!!.getName() == nameID1)
        assertTrue(list2!!.getName() == nameID2)
        assertTrue(list3!!.getName() == nameID3)
        assertTrue(list4!!.getName() == nameID4)

    }

    @Test
    fun addPrevListAndGetLists_correctIDSet() {
        val page = MainPage(listRepository = ListRepositoryMock(), mainRepository = MainRepositoryMock())

        val nameID0 = "list1"
        val nameID1 = "list2"
        val nameID2 = "list3"
        val nameID3 = "list4"

        runBlocking {
            page.addList(
                TaskTypes.PRIORITY,
                nameID0,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
            page.addList(
                TaskTypes.PRIORITY,
                nameID3,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
            page.prevList()
            page.addList(
                TaskTypes.PRIORITY,
                nameID2,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
            page.prevList()
            page.addList(
                TaskTypes.PRIORITY,
                nameID1,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
        }

        val list0 = page.getListByID(0)
        val list1 = page.getListByID(1)
        val list2 = page.getListByID(2)
        val list3 = page.getListByID(3)


        assertTrue(list0!!::class == TaskTypes.PRIORITY.listType)
        assertTrue(list1!!::class == TaskTypes.PRIORITY.listType)
        assertTrue(list2!!::class == TaskTypes.PRIORITY.listType)
        assertTrue(list3!!::class == TaskTypes.PRIORITY.listType)

        assertTrue((list0 as PriorityTaskList).getName() == nameID0)
        assertTrue((list1 as PriorityTaskList).getName() == nameID1)
        assertTrue((list2 as PriorityTaskList).getName() == nameID2)
        assertTrue((list3 as PriorityTaskList).getName() == nameID3)
    }

    @Test
    fun addList_initialReadingOfCurrentList_currentListIDCurrentListCurrentTypeSetCorrectly() {
        val page = MainPage(listRepository = ListRepositoryMock(), mainRepository = MainRepositoryMock())

        val name = "name"

        runBlocking {
            page.addList(
                TaskTypes.PRIORITY,
                name,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
        }

        assertTrue(page.currentType == TaskTypes.PRIORITY)
        assertTrue((page.currentList as PriorityTaskList).getName() == name)
        assertTrue(page.currentListID == 0)

    }

    @Test
    fun addListPrevListGetList_addingCoupleOfLists_currentListIDCurrentListCurrentTypeSetCorrectly() {
        val page = MainPage(listRepository = ListRepositoryMock(), mainRepository = MainRepositoryMock())

        val name1 = "name1"; val type1 = TaskTypes.PRIORITY
        val name2 = "name2"; val type2 = TaskTypes.DEADLINE_CATEGORY
        val name3 = "name3"; val type3 = TaskTypes.DEADLINE_PRIORITY
        val name4 = "name4"; val type4 = TaskTypes.DEADLINE
        val name5 = "name5"; val type5 = TaskTypes.CATEGORY

        runBlocking {
            page.addList(
                type1,
                name1,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
            page.prevList()
            page.addList(
                type2,
                name2,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
            page.prevList()
            page.addList(
                type3,
                name3,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
            page.prevList()
            page.addList(
                type4,
                name4,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
            page.prevList()
            page.addList(
                type5,
                name5,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
            page.prevList()
            page.changeIDofCurrentList(5)
        }

        val list5 = page.getListByID(0)
        val list4 = page.getListByID(1)
        val list3 = page.getListByID(2)
        val list2 = page.getListByID(3)
        val list1 = page.getListByID(4)

        assertTrue(page.currentListID == 4)
        assertNotNull(page.currentList)
        assertTrue(page.currentList!!::class  == type1.listType)

        assertTrue((list5 as TaskList<Task>).getName() == name5)
        assertTrue((list4 as TaskList<Task>).getName() == name4)
        assertTrue((list3 as TaskList<Task>).getName() == name3)
        assertTrue((list2 as TaskList<Task>).getName() == name2)
        assertTrue((list1 as TaskList<Task>).getName() == name1)

    }

    @Test
    fun addListGetList_BadIndex_ArrayIndexOutBoundsExceptionThrown() {
        val page = MainPage(listRepository = ListRepositoryMock(), mainRepository = MainRepositoryMock())

        val name1 = "name1"; val type1 = TaskTypes.PRIORITY
        val name2 = "name2"; val type2 = TaskTypes.DEADLINE_CATEGORY
        val name3 = "name3"; val type3 = TaskTypes.DEADLINE_PRIORITY
        val name4 = "name4"; val type4 = TaskTypes.DEADLINE
        val name5 = "name5"; val type5 = TaskTypes.CATEGORY

        runBlocking {
            page.addList(
                type1,
                name1,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
            page.prevList()
            page.addList(
                type2,
                name2,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
            page.prevList()
            page.addList(
                type3,
                name3,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
            page.prevList()
            page.addList(
                type4,
                name4,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
            page.prevList()
            page.addList(
                type5,
                name5,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
            page.prevList()
        }


        assertThrows(IndexOutOfBoundsException::class.java){
            page.getListByID(10)
        }
        assertThrows(IndexOutOfBoundsException::class.java){
            page.getListByID(5)
        }
        assertThrows(IndexOutOfBoundsException::class.java){
            page.getListByID(-1)
        }
    }

    @Test
    fun addListsAndTriggerNextList_NextListSetsCurrentVariablesCorrectly() {
        val page = MainPage(listRepository = ListRepositoryMock(), mainRepository = MainRepositoryMock())

        val name1 = "name1"; val type1 = TaskTypes.PRIORITY
        val name2 = "name2"; val type2 = TaskTypes.DEADLINE_CATEGORY
        val name3 = "name3"; val type3 = TaskTypes.DEADLINE_PRIORITY
        val name4 = "name4"; val type4 = TaskTypes.DEADLINE
        val name5 = "name5"; val type5 = TaskTypes.CATEGORY

        runBlocking {
            page.addList(
                type1,
                name1,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
            page.prevList()
            page.addList(
                type2,
                name2,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
            page.prevList()
            page.addList(
                type3,
                name3,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
            page.prevList()
            page.addList(
                type4,
                name4,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
            page.prevList()
            page.addList(
                type5,
                name5,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
            page.prevList()
        }

        page.nextList()

        assertTrue(page.currentListID == 1)
        assertTrue((page.currentList as TaskList<Task>).getName() == name5)
        assertTrue(page.currentType == type5)

        page.nextList()

        assertTrue(page.currentListID == 2)
        assertTrue((page.currentList as TaskList<Task>).getName() == name4)
        assertTrue(page.currentType == type4)

        page.nextList()

        assertTrue(page.currentListID == 3)
        assertTrue((page.currentList as TaskList<Task>).getName() == name3)
        assertTrue(page.currentType == type3)

        page.nextList()

        assertTrue(page.currentListID == 4)
        assertTrue((page.currentList as TaskList<Task>).getName() == name2)
        assertTrue(page.currentType == type2)

    }

    @Test
    fun addListAndNextList_CurrentVariablesSetToLastList() {
        val page = MainPage(listRepository = ListRepositoryMock(), mainRepository = MainRepositoryMock())

        val name1 = "name1"; val type1 = TaskTypes.PRIORITY
        val name2 = "name2"; val type2 = TaskTypes.DEADLINE_CATEGORY

        runBlocking {
            page.addList(
                type1,
                name1,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
            page.addList(
                type2,
                name2,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
        }

        page.nextList()

        assertTrue(page.currentListID == 1)
        assertTrue((page.currentList as TaskList<Task>).getName() == name2)
        assertTrue(page.currentType == type2)
    }

    @Test
    fun addListAndPrevList_CurrentVariablesSetToFirstList() {
        val page = MainPage(listRepository = ListRepositoryMock(), mainRepository = MainRepositoryMock())

        val name1 = "name1"; val type1 = TaskTypes.PRIORITY
        val name2 = "name2"; val type2 = TaskTypes.DEADLINE_CATEGORY

        runBlocking {
            page.addList(
                type1,
                name1,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
            page.addList(
                type2,
                name2,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
        }

        page.prevList()
        page.prevList()
        page.prevList()

        assertTrue(page.currentListID == 0)
        assertTrue((page.currentList as TaskList<Task>).getName() == name1)
        assertTrue(page.currentType == type1)
    }

    @Test
    fun deleteCurrentListInTheMiddle_onDelete_listDeletedAndPreviousListSetAsCurrent() {
        val page = MainPage(listRepository = ListRepositoryMock(), mainRepository = MainRepositoryMock())

        val name1 = "name1"; val type1 = TaskTypes.PRIORITY
        val name2 = "name2"; val type2 = TaskTypes.DEADLINE_CATEGORY
        val name3 = "name3"; val type3 = TaskTypes.DEADLINE_PRIORITY
        val name4 = "name4"; val type4 = TaskTypes.DEADLINE

        runBlocking {
            page.addList(
                type1,
                name1,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
            page.addList(
                type2,
                name2,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
            page.addList(
                type3,
                name3,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
            page.addList(
                type4,
                name4,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
            page.prevList()

            page.deleteCurrentList()
        }

        assertTrue(page.currentListID == 2)
        assertTrue(page.currentType == type4)
        assertTrue((page.currentList as DeadlineTaskList).getName() == name4)
    }

    @Test
    fun deleteCurrentListAtTheEnd_onDelete_listDeletedAndPreviousListSetAsCurrent() {
        val page = MainPage(listRepository = ListRepositoryMock(), mainRepository = MainRepositoryMock())

        val name1 = "name1"; val type1 = TaskTypes.PRIORITY
        val name2 = "name2"; val type2 = TaskTypes.DEADLINE_CATEGORY
        val name3 = "name3"; val type3 = TaskTypes.DEADLINE_PRIORITY
        val name4 = "name4"; val type4 = TaskTypes.DEADLINE

        runBlocking {
            page.addList(
                type1,
                name1,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
            page.addList(
                type2,
                name2,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
            page.addList(
                type3,
                name3,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
            page.addList(
                type4,
                name4,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )

            page.deleteCurrentList()
        }

        assertTrue(page.currentListID == 2)
        assertTrue(page.currentType == TaskTypes.DEADLINE_PRIORITY)
        assertTrue((page.currentList as DeadlinePriorityTaskList).getName() == name3)
    }

    @Test
    fun deleteCurrentListAtTheStart_onDelete_listDeletedAndPreviousListSetAsCurrent() {
        val page = MainPage(listRepository = ListRepositoryMock(), mainRepository = MainRepositoryMock())

        val name1 = "name1"; val type1 = TaskTypes.PRIORITY
        val name2 = "name2"; val type2 = TaskTypes.DEADLINE_CATEGORY
        val name3 = "name3"; val type3 = TaskTypes.DEADLINE_PRIORITY
        val name4 = "name4"; val type4 = TaskTypes.DEADLINE

        runBlocking {
            page.addList(
                type1,
                name1,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
            page.addList(
                type2,
                name2,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
            page.addList(
                type3,
                name3,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
            page.addList(
                type4,
                name4,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
            page.prevList()
            page.prevList()
            page.prevList()

            page.deleteCurrentList()
        }

        assertTrue(page.currentListID == 0)
        assertTrue(page.currentType == TaskTypes.DEADLINE_CATEGORY)
        assertTrue((page.currentList as DeadlineCategoryTaskList).getName() == name2)
    }

    @Test
    fun addAndDeleteList_EmptyPage() {
        val page = MainPage(listRepository = ListRepositoryMock(), mainRepository = MainRepositoryMock())
        val name1 = "name1"; val type1 = TaskTypes.PRIORITY

        runBlocking {
            page.addList(
                type1,
                name1,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
            page.deleteCurrentList()
        }

        assertTrue(page.currentList == null)
        assertTrue(page.currentType == null)

    }

    @Test
    fun changeIDofCurrentList_onListOrderChange_listIDAndOrderChanged() {
        val page = MainPage(listRepository = ListRepositoryMock(), mainRepository = MainRepositoryMock())

        val name1 = "name1"; val type1 = TaskTypes.PRIORITY
        val name2 = "name2"; val type2 = TaskTypes.DEADLINE_CATEGORY
        val name3 = "name3"; val type3 = TaskTypes.DEADLINE_PRIORITY
        val name4 = "name4"; val type4 = TaskTypes.DEADLINE

        runBlocking {
            page.addList(
                type1,
                name1,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
            page.addList(
                type2,
                name2,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
            page.addList(
                type3,
                name3,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )
            page.addList(
                type4,
                name4,
                SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
            )

            page.changeIDofCurrentList(0)
            page.nextList()
        }

        assertTrue(page.currentListID == 1)
        assertTrue(page.currentType == type1)
        assertTrue((page.currentList as PriorityTaskList).getName() == name1)

        runBlocking {
            page.changeIDofCurrentList(2)
            page.prevList()
        }

        assertTrue(page.currentListID == 1)
        assertTrue(page.currentType == type2)
        assertTrue((page.currentList as DeadlineCategoryTaskList).getName() == name2)


    }
}