package com.example.prioritylist

import androidx.compose.ui.graphics.Color
import com.example.prioritylist.data.backend.*
import com.example.prioritylist.data.backend.Category
import com.example.prioritylist.data.backend.StatusCodes
import junit.framework.TestCase.*
import org.junit.*
import org.junit.Assert.assertThrows
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

class TaskListTests {

    @Test
    fun addAndGetTaskByID_taskAddedAndRead_CorrectTaskRememberedAndProperIDSet() {
        val list = PriorityTaskList(name = "test", id = 0, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"))
        val task1 = PriorityTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1212:15:30"),
            name = "test_name_1",
            description = "desc_1",
            id = 3,
            priority = 1)
        list.add(task1)
        val returnedTask = list.getTaskByID(0)
        assertTrue(
            returnedTask.name == task1.name &&
            returnedTask.description == task1.description &&
            returnedTask.dateOfCreation == task1.dateOfCreation
        )
    }

    @Test
    fun addAndGetTaskByName_taskAddedAndRead_CorrectTaskRemembered(){
        val list = PriorityTaskList(name = "test", id = 0, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"))
        val task1 = PriorityTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1212:15:30"),
            name = "test_name_1",
            description = "desc_1",
            id = 3,
            priority = 1
        )
        val task2 = PriorityTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-2412:15:30"),
            name = "test_name_2",
            description = "desc_2",
            id = 3,
            priority = 1
        )
        val task3 = PriorityTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-3012:15:30"),
            name = "test_name_3",
            description = "desc_3",
            id = 2,
            priority = 5
        )

        list.add(task1)
        list.add(task3)
        list.add(task1)
        list.add(task2)

        val returnedTask1 = list.getTaskByName("test_name_1")
        val returnedTask2 = list.getTaskByName("test_name_2")
        val returnedTask3 = list.getTaskByName("test_name_3")


        assertTrue(
            returnedTask1!!.name == task1.name &&
                    returnedTask1!!.description == task1.description &&
                    returnedTask1!!.dateOfCreation == task1.dateOfCreation
        )


        assertTrue(
            returnedTask2!!.name == task2.name &&
                    returnedTask2!!.description == task2.description &&
                    returnedTask2!!.dateOfCreation == task2.dateOfCreation
        )


        assertTrue(
            returnedTask3!!.name == task3.name &&
                    returnedTask3!!.description == task3.description &&
                    returnedTask3!!.dateOfCreation == task3.dateOfCreation
        )
    }

    @Test
    fun add_checkForUniqueness_duplicatedTaskCodeReturned() {
        val list = PriorityTaskList(name = "test", id = 0, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"))
        val task1 = PriorityTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1212:15:30"),
            name = "test_name_1",
            description = "desc_1",
            id = 3,
            priority = 1
        )
        val task2 = PriorityTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-2412:15:30"),
            name = "test_name_1",
            description = "desc_2",
            id = 3,
            priority = 1
        )
        val task3 = PriorityTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1212:15:30"),
            name = "test_name_3",
            description = "desc_1",
            id = 3,
            priority = 1
        )
        list.add(task1)
        val code1 = list.add(task2)
        val code2 = list.add(task3)

        assertTrue(code1.code == StatusCodes.DUPLICATED_TASK)
        assertTrue(code2.code == StatusCodes.SUCCESS)



    }

    @Test
    fun priorityGetPriority1_whenPriorityNeedsToBeEvaluated_correctOrdering() {
        val list = PriorityTaskList(name = "test", id = 0, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"))
        val taskByDate1 = PriorityTask(
            dateOfCreation = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-04-1212:15:30"),
            name = "test_name_1",
            description = "desc_1",
            id = 3,
            priority = 1
        )
        val taskByDate2 = PriorityTask(
            dateOfCreation = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1212:15:35"),
            name = "test_name_2",
            description = "desc_2",
            id = 3,
            priority = 1
        )
        val taskByDate3 = PriorityTask(
            dateOfCreation = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1212:15:30"),
            name = "test_name_3",
            description = "desc_3",
            id = 2,
            priority = 1
        )

        list.add(taskByDate2)
        list.add(taskByDate1)
        list.add(taskByDate3)

        val firstTaskByDate = list.getTaskByID(0)
        val secondTaskByDate = list.getTaskByID(1)
        val thirdTaskByDate = list.getTaskByID(2)



        assertTrue(
            firstTaskByDate.name == taskByDate3.name &&
                    firstTaskByDate.description == taskByDate3.description &&
                    firstTaskByDate.priority == taskByDate3.priority
        )
        assertTrue(
            secondTaskByDate.name == taskByDate2.name &&
                    secondTaskByDate.description == taskByDate2.description &&
                    secondTaskByDate.priority == taskByDate2.priority
        )
        assertTrue(
            thirdTaskByDate.name == taskByDate1.name &&
                    thirdTaskByDate.description == taskByDate1.description &&
                    thirdTaskByDate.priority == taskByDate1.priority
        )

    }

    @Test
    fun priorityGetPriority2_whenPriorityNeedsToBeEvaluated_correctOrdering() {
        val list = PriorityTaskList(name = "test", id = 0, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"))
        val taskByPriority1 = PriorityTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1212:15:30"),
            name = "test_name_1",
            description = "desc_1",
            id = 3,
            priority = 4
        )
        val taskByPriority2 = PriorityTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1212:15:30"),
            name = "test_name_2",
            description = "desc_2",
            id = 3,
            priority = 3
        )
        val taskByPriority3 = PriorityTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1212:15:30"),
            name = "test_name_3",
            description = "desc_3",
            id = 2,
            priority = 2
        )



        list.add(taskByPriority1)
        list.add(taskByPriority2)
        list.add(taskByPriority3)

        val firstTaskByPriority = list.getTaskByID(0)
        val secondTaskByPriority = list.getTaskByID(1)
        val thirdTaskByPriority = list.getTaskByID(2)



        assertTrue(
            firstTaskByPriority.name == taskByPriority1.name &&
                    firstTaskByPriority.description == taskByPriority1.description &&
                    firstTaskByPriority.priority == taskByPriority1.priority
        )
        assertTrue(
            secondTaskByPriority.name == taskByPriority2.name &&
                    secondTaskByPriority.description == taskByPriority2.description &&
                    secondTaskByPriority.priority == taskByPriority2.priority
        )
        assertTrue(
            thirdTaskByPriority.name == taskByPriority3.name &&
                    thirdTaskByPriority.description == taskByPriority3.description &&
                    thirdTaskByPriority.priority == taskByPriority3.priority
        )


    }

    @Test
    fun deadlineGetPriority1_whenPriorityNeedsToBeEvaluated_correctOrdering() {
        val list = DeadlineTaskList(name = "test", id = 0, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"))
        val taskByDate1 = DeadlineTask(
            dateOfCreation = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-01-1212:15:30"),
            name = "test_name_1",
            description = "desc_1",
            id = 3,
            deadline = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-04-1212:15:30")
        )
        val taskByDate2 = DeadlineTask(
            dateOfCreation = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-02-1212:15:30"),
            name = "test_name_2",
            description = "desc_2",
            id = 3,
            deadline = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-05-1212:15:30")
        )
        val taskByDate3 = DeadlineTask(
            dateOfCreation = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1212:15:30"),
            name = "test_name_3",
            description = "desc_3",
            id = 2,
            deadline = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-06-1212:15:30")
        )

        list.add(taskByDate2)
        list.add(taskByDate1)
        list.add(taskByDate3)

        val firstTaskByDate = list.getTaskByID(0)
        val secondTaskByDate = list.getTaskByID(1)
        val thirdTaskByDate = list.getTaskByID(2)



        assertTrue(firstTaskByDate == taskByDate1)
        assertTrue(secondTaskByDate == taskByDate2)
        assertTrue(thirdTaskByDate == taskByDate3)

    }

    @Test
    fun deadlineGetPriority2__whenPriorityNeedsToBeEvaluated_correctOrdering(){

        val list = DeadlineTaskList(name = "test", id = 0, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"))

        val taskByDeadline1 = DeadlineTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1212:15:30"),
            name = "test_name_1",
            description = "desc_1",
            id = 3,
            deadline = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-06-1212:15:30")
        )
        val taskByDeadline2 = DeadlineTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-04-1212:15:30"),
            name = "test_name_2",
            description = "desc_2",
            id = 3,
            deadline = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-06-1312:15:30")
        )
        val taskByDeadline3 = DeadlineTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-05-1212:15:30"),
            name = "test_name_3",
            description = "desc_3",
            id = 2,
            deadline = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-12-1212:15:30")
        )

        list.add(taskByDeadline1)
        list.add(taskByDeadline2)
        list.add(taskByDeadline3)

        val firstTaskByPriority = list.getTaskByID(0)
        val secondTaskByPriority = list.getTaskByID(1)
        val thirdTaskByPriority = list.getTaskByID(2)



        assertTrue(firstTaskByPriority == taskByDeadline1)
        assertTrue(secondTaskByPriority == taskByDeadline2)
        assertTrue(thirdTaskByPriority == taskByDeadline3)


    }

    @Test
    fun categoryGetPriority1_whenPriorityNeedsToBeEvaluated_correctOrdering() {
        val list = CategoryTaskList(name = "test", id = 0, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"))
        val cat1 = Category(
            name = "cat1",
            priority = 1,
            color = Color(12),
            description = "desc1"
        )
        val cat2 = Category(
            name = "cat2",
            priority = 1,
            color = Color(32),
            description = "desc2"
        )
        val cat3 = Category(
            name = "cat3",
            priority = 4,
            color = Color(512),
            description = "desc3"
        )


        val taskByDate1 = CategoryTask(
            dateOfCreation = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-01-1212:15:30"),
            name = "test_name_1",
            description = "desc_1",
            id = 3,
            category = cat1
        )
        val taskByDate2 = CategoryTask(
            dateOfCreation = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-02-1212:15:30"),
            name = "test_name_2",
            description = "desc_2",
            id = 3,
            category = cat2
        )
        val taskByDate3 = CategoryTask(
            dateOfCreation = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1212:15:30"),
            name = "test_name_3",
            description = "desc_3",
            id = 2,
            category = cat3
        )

        list.add(taskByDate2)
        list.add(taskByDate1)
        list.add(taskByDate3)

        val firstTaskByDate = list.getTaskByID(0)
        val secondTaskByDate = list.getTaskByID(1)
        val thirdTaskByDate = list.getTaskByID(2)


        assertTrue(firstTaskByDate == taskByDate1)
        assertTrue(secondTaskByDate == taskByDate2)
        assertTrue(thirdTaskByDate == taskByDate3)

    }

    @Test
    fun categoryGetPriority2_whenPriorityNeedsToBeEvaluated_correctOrdering(){
        val list = CategoryTaskList(name = "test", id = 0, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"))

        val cat1 = Category(
            name = "cat1",
            priority = 1,
            color = Color(12),
            description = "desc1"
        )
        val cat2 = Category(
            name = "cat2",
            priority = 1,
            color = Color(32),
            description = "desc2"
        )
        val cat3 = Category(
            name = "cat3",
            priority = 4,
            color = Color(512),
            description = "desc3"
        )

        val taskByCategory1 = CategoryTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-01-1212:15:30"),
            name = "test_name_1",
            description = "desc_1",
            id = 3,
            category =  cat1
        )
        val taskByCategory2 = CategoryTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-02-1212:15:30"),
            name = "test_name_2",
            description = "desc_2",
            id = 3,
            category =  cat2
        )
        val taskByCategory3 = CategoryTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1212:15:30"),
            name = "test_name_3",
            description = "desc_3",
            id = 2,
            category =  cat3
        )

        list.add(taskByCategory1)
        list.add(taskByCategory2)
        list.add(taskByCategory3)

        val firstTaskByCategory = list.getTaskByID(0)
        val secondTaskByCategory = list.getTaskByID(1)
        val thirdTaskByCategory = list.getTaskByID(2)


        assertTrue(
            firstTaskByCategory.name == taskByCategory1.name &&
                    firstTaskByCategory.description == taskByCategory1.description &&
                    firstTaskByCategory.category == taskByCategory1.category
        )
        assertTrue(
            secondTaskByCategory.name == taskByCategory2.name &&
                    secondTaskByCategory.description == taskByCategory2.description &&
                    secondTaskByCategory.category == taskByCategory2.category
        )
        assertTrue(
            secondTaskByCategory.name == taskByCategory3.name &&
                    secondTaskByCategory.description == taskByCategory3.description &&
                    secondTaskByCategory.category == taskByCategory3.category
        )


    }

    @Test
    fun deadlineCategoryGetPriority1_whenPriorityNeedsToBeEvaluated_correctOrdering() {
        val list = DeadlineCategoryTaskList(name = "test", id = 0, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"))
        val cat1 = Category(
            name = "cat1",
            priority = 1,
            color = Color(12),
            description = "desc1"
        )
        val cat2 = Category(
            name = "cat2",
            priority = 1,
            color = Color(32),
            description = "desc2"
        )
        val cat3 = Category(
            name = "cat3",
            priority = 4,
            color = Color(512),
            description = "desc3"
        )


        val taskByDate1 = DeadlineCategoryTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-01-1212:15:30"),
            name = "test_name_1",
            description = "desc_1",
            id = 3,
            category = cat1,
            deadline = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-01-1312:15:30")
        )
        val taskByDate2 = DeadlineCategoryTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-02-1212:15:30"),
            name = "test_name_2",
            description = "desc_2",
            id = 3,
            category = cat2,
            deadline = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-02-1312:15:30")
        )
        val taskByDate3 = DeadlineCategoryTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1212:15:30"),
            name = "test_name_3",
            description = "desc_3",
            id = 2,
            category = cat3,
            deadline = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1312:15:30")
        )

        list.add(taskByDate2)
        list.add(taskByDate1)
        list.add(taskByDate3)

        val firstTaskByDate = list.getTaskByID(0)
        val secondTaskByDate = list.getTaskByID(1)
        val thirdTaskByDate = list.getTaskByID(2)


        assertTrue(
            firstTaskByDate.name == taskByDate1.name &&
                    firstTaskByDate.description == taskByDate1.description &&
                    firstTaskByDate.category == taskByDate1.category
        )
        assertTrue(
            secondTaskByDate.name == taskByDate2.name &&
                    secondTaskByDate.description == taskByDate2.description &&
                    secondTaskByDate.category == taskByDate2.category
        )
        assertTrue(
            thirdTaskByDate.name == taskByDate3.name &&
                    thirdTaskByDate.description == taskByDate3.description &&
                    thirdTaskByDate.category == taskByDate3.category
        )
    }

    @Test
    fun delete_onDeleteOrEdit_taskDeleted() {
        val list = DeadlineTaskList(name = "test", id = 0, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"))

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

        list.add(task1)
        list.add(task2)
        list.add(task3)

        list.delete(task1)

        assertThrows(java.util.NoSuchElementException::class.java) {
            val ret1 = list.getTaskByName(task1.name)
        }

        list.delete(task3)
        val ret2 = list.getTaskByName(task2.name)

        assertThrows(java.util.NoSuchElementException::class.java) {
            val ret3 = list.getTaskByName(task3.name)
        }

        assertTrue(ret2!!.name == task2.name)

    }

    @Test
    fun addTask_taskAdded_CorrectTaskAdded() {
        val list = PriorityTaskList(name = "test", id = 0, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"))
        val task = PriorityTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1212:15:30"),
            name = "test_name_1",
            description = "desc_1",
            id = 3,
            priority = 1)
        list.addTask(task)
        val returnedTask = list.getTaskByID(0)

        assertTrue(returnedTask == task)
    }

    @Test
    fun editTask_taskEdited_taskProperlyEdited() {
        val list = PriorityTaskList(name = "test", id = 0, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"))
        val task = PriorityTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1212:15:30"),
            name = "test_name_1",
            description = "desc_1",
            id = 3,
            priority = 1
        )
        list.add(task)
        val editedTask = PriorityTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-03-1212:15:30"),
            name = "edited_task",
            description = "desc_new",
            id = 3,
            priority = 3
        )
        list.editTask(0, editedTask)

        val returnedTask = list.getTaskByID(0)

        assertTrue(returnedTask == editedTask)

    }

    @Test
    fun deleteTask_taskDeleted_properTaskDeleted() {
        val list = DeadlineTaskList(name = "test", id = 0, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"))

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

        list.add(task1)
        list.add(task2)
        list.add(task3)

        list.deleteTask(task1)

        assertThrows(java.util.NoSuchElementException::class.java) {
            val ret1 = list.getTaskByName(task1.name)
        }

        list.deleteTask(task3)
        val ret2 = list.getTaskByName(task2.name)

        assertThrows(java.util.NoSuchElementException::class.java) {
            val ret3 = list.getTaskByName(task3.name)
        }

        assertTrue(ret2!!.name == task2.name)
    }

    @Test
    fun changeID_onIDChange_IDChanged() {
        val list = PriorityTaskList(
            name = "test1",
            id = 0,
            SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
        )
        val newId = 12
        list.changeID(newId)

        assertTrue(list.getID() == newId)
    }

    @Test
    fun changeName_onNameChange_nameChanged() {
        val list = PriorityTaskList(
            name = "old",
            id = 0,
            SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
        )
        val new = "new"
        list.changeName(new)

        assertTrue(list.getName() == new)
    }

    @Test
    fun getList_listSharing_listOfTasksReturned() {
        val list = DeadlineTaskList(name = "test", id = 0, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"))

        val task1 = DeadlineTask(
            name = "test1",
            description = "desc",
            id = 0,
            dateOfCreation = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-0612:15:30"),
            deadline = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2024-11-0412:15:30")
        )

        val task2 = DeadlineTask(
            name = "test2",
            description = "desc",
            id = 0,
            dateOfCreation = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1212:15:30"),
            deadline = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2026-12-1112:15:30")
        )

        val task3 = DeadlineTask(
            name = "test3",
            description = "desc",
            id = 0,
            dateOfCreation = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-08-0612:15:30"),
            deadline = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-02-1212:15:30")
        )

        list.add(task1)
        list.add(task2)
        list.add(task3)
        list.add(task1)

        val returnedList = list.getList()
        returnedList.sortBy { it.name }
        assertTrue(returnedList[0] == task1)
        assertTrue(returnedList[2] == task2)
        assertTrue(returnedList[3] == task3)
    }

    @Test
    fun getList_listSharingWithCopying_copyOflistOfTasksReturned() {
        val list = DeadlineTaskList(name = "test", id = 0, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"))

        val task1 = DeadlineTask(
            name = "test1",
            description = "desc",
            id = 0,
            dateOfCreation = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-0612:15:30"),
            deadline = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2024-11-0412:15:30")
        )

        val task2 = DeadlineTask(
            name = "test2",
            description = "desc",
            id = 0,
            dateOfCreation = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1212:15:30"),
            deadline = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2026-12-1112:15:30")
        )

        val task3 = DeadlineTask(
            name = "test3",
            description = "desc",
            id = 0,
            dateOfCreation = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-08-0612:15:30"),
            deadline = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-02-1212:15:30")
        )

        list.add(task1)
        list.add(task2)
        list.add(task3)

        val returnedList = list.getList()
        returnedList.sortBy { it.name }
        returnedList.removeAt(0)

        val returnedList1 = list.getList()
        returnedList1.sortBy { it.name }
        assertTrue(returnedList[0] != returnedList1[0])
    }

    @Test
    fun pushAndPop_onAction_ActionPushed(){
        val storage = Storage<PriorityTask>()

        val addedTask = PriorityTask(
            priority = 1,
            dateOfCreation = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1612:15:30"),
            evaluatedPriority = 3.0,
            id = 5,
            description = "desc",
            name = "test"
        )

        val action = Add<PriorityTask>(addedTask)

        storage.push(action)

        val returnedAction = storage.pop()

        assertTrue(returnedAction == action)

    }

    @Test
    fun undoAdd_onUndoingAdding_taskReverseDeleted() {
        val list = PriorityTaskList(
            name = "test",
            id = 0,
            SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
        )

        val addedTask = PriorityTask(
            priority = 1,
            dateOfCreation = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1612:15:30"),
            evaluatedPriority = 3.0,
            id = 5,
            description = "desc",
            name = "test"
        )

        list.addTask(addedTask)
        list.undo()

        assertThrows(IndexOutOfBoundsException::class.java) {
            val returnedTask = list.getTaskByID(0)
        }

    }

    @Test
    fun undoDelete_onRestoringTask_taskRestored() {
        val list = PriorityTaskList(
            name = "test",
            id = 0,
            SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
        )

        val addedTask = PriorityTask(
            priority = 1,
            dateOfCreation = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1612:15:30"),
            evaluatedPriority = 3.0,
            id = 5,
            description = "desc",
            name = "test"
        )

        list.addTask(addedTask)
        list.deleteTask(list.getTaskByID(0))
        list.undo()

        val task = list.getTaskByID(0)

        assertTrue(task == addedTask)

        assertThrows(IndexOutOfBoundsException::class.java){
            val nonexistentTask = list.getTaskByID(1)
        }
    }

    @Test
    fun undoEdit_onRestoringPreviousVersionOfTask_taskVersionRestored() {
        val list = PriorityTaskList(
            name = "test",
            id = 0,
            SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
        )

        val addedTask = PriorityTask(
            priority = 1,
            dateOfCreation = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1612:15:30"),
            evaluatedPriority = 3.0,
            id = 5,
            description = "desc",
            name = "test"
        )

        val editedTask = PriorityTask(
            priority = 5,
            dateOfCreation = SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1712:34:30"),
            evaluatedPriority = 5.0,
            id = 2,
            description = "newDesc",
            name = "editedTask"
        )

        list.add(addedTask)
        list.editTask(0, editedTask)

        list.undo()

        val returnedTask = list.getTaskByID(0)

        assertTrue(returnedTask == addedTask)

        assertThrows(IndexOutOfBoundsException::class.java) {
            val nonexistentTask = list.getTaskByID(1)
        }

    }

    @Test
    fun undoMix() {
        val list = PriorityTaskList(
            name = "test",
            id = 0,
            SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30")
        )

        val task1 = PriorityTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1212:15:30"),
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
            priority = 4
        )
        val task3 = PriorityTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1212:15:30"),
            name = "test_name_3",
            description = "desc_1",
            id = 3,
            priority = 5
        )
        val task4 = PriorityTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1212:15:30"),
            name = "test_name_4",
            description = "desc_1",
            id = 3,
            priority = 6
        )
        val task5 = PriorityTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-2412:15:30"),
            name = "test_name_5",
            description = "desc_2",
            id = 3,
            priority = 7
        )
        val task6 = PriorityTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1212:15:30"),
            name = "test_name_6",
            description = "desc_1",
            id = 3,
            priority = 8
        )
        val task7 = PriorityTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1212:15:30"),
            name = "test_name_7",
            description = "desc_1",
            id = 3,
            priority = 9
        )
        val task8 = PriorityTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-2412:15:30"),
            name = "test_name_8",
            description = "desc_2",
            id = 3,
            priority = 10
        )
        val task9 = PriorityTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1212:15:30"),
            name = "test_name_9",
            description = "desc_1",
            id = 3,
            priority = 11
        )

        list.addTask(task1)
        list.addTask(task2)
        list.undo()

        assertTrue((list.getList().size == 1) and (list.getList()[0] == task1))

        list.addTask(task3)
        list.addTask(task2)
        list.editTask(0, task4)
        list.undo()

        assertTrue(list.getList()[0] == task3)

        list.deleteTask(task1)
        list.addTask(task5)
        list.undo()

        assertTrue((list.getList().size == 2) and (list.getList()[0] != task5))

        list.undo()

        assertTrue((list.getList().size == 3) and (list.getList()[2] == task1))


    }

    @Test
    fun delete_tasksDeleted() {
        val list = PriorityTaskList("name", 0, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"))

        val task1 = PriorityTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1212:15:30"),
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

        list.addTask(task1)
        list.addTask(task2)
        list.addTask(task3)
        list.addTask(task4)
        list.addTask(task5)
        list.addTask(task6)
        list.addTask(task7)
        list.addTask(task8)
        list.addTask(task9)

        val returnedList = list.getList()

        list.deleteTask(returnedList[0])
        list.deleteTask(returnedList[1])
        list.deleteTask(returnedList[2])
        list.deleteTask(returnedList[3])
        list.deleteTask(returnedList[4])
        list.deleteTask(returnedList[5])
        list.deleteTask(returnedList[6])
        list.deleteTask(returnedList[7])
        list.deleteTask(returnedList[8])


    }

    @Test
    fun addTasks_properOrder() {
        val list = PriorityTaskList("name", 0, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2022-12-1612:15:30"))

        val task1 = PriorityTask(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1212:15:30"),
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

        list.addTask(task1)
        list.addTask(task2)
        list.addTask(task3)
        list.addTask(task4)
        list.addTask(task5)
        list.addTask(task6)
        list.addTask(task7)
        list.addTask(task8)
        list.addTask(task9)

        val returnedList = list.getList()

        assertTrue(returnedList[0] == task4)
        assertTrue(returnedList[1] == task6)
        assertTrue(returnedList[2] == task7 || returnedList[2] == task1)
        assertTrue(returnedList[3] == task7 || returnedList[3] == task1)
        assertTrue(returnedList[4] == task8 || returnedList[4] == task5 || returnedList[4] == task2)
        assertTrue(returnedList[5] == task8 || returnedList[5] == task5 || returnedList[5] == task2)
        assertTrue(returnedList[6] == task8 || returnedList[6] == task5 || returnedList[6] == task2)
        assertTrue(returnedList[7] == task3 || returnedList[7] == task9)

    }



}