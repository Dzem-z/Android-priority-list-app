package com.example.prioritylist

import com.example.prioritylist.data.HistoryList
import com.example.prioritylist.data.PriorityTask
import com.example.prioritylist.data.Task
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import org.junit.Test
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class HistoryTests {

    @Test
    fun pushTaskAndGetByID_onMoveToHistory_taskAdded() {
        val historyList = HistoryList<Task> ()

        val task1 = Task(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-01-1612:15:30"),
            name = "test_name_1",
            description = "desc_1",
            id = 3
        )
        val task2 = Task(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-02-1612:15:30"),
            name = "test_name_2",
            description = "desc_2",
            id = 3
        )
        val task3 = Task(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1612:15:30"),
            name = "test_name_3",
            description = "desc_3",
            id = 2
        )

        historyList.pushTask(task1)
        historyList.pushTask(task2)
        historyList.pushTask(task3)

        val returnedTask1 = historyList.getTaskByID(0)
        val returnedTask2 = historyList.getTaskByID(1)
        val returnedTask3 = historyList.getTaskByID(2)
        val returnedTask4 = historyList.getTaskByID(10)


        assertNotNull(returnedTask1)
        assertNotNull(returnedTask2)
        assertNotNull(returnedTask3)

        assertTrue(returnedTask4 == null)
        assertTrue(returnedTask3 == task1)
        assertTrue(returnedTask2 == task2)
        assertTrue(returnedTask1 == task3)
    }

    @Test
    fun addAndGetList_onReadRequest_TasksAddedAndProperListReturned() {
        val historyList = HistoryList<Task> ()
        val name1 = "test_name_1"
        val name2 = "test_name_2"
        val name3 = "test_name_3"

        val task1 = Task(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-01-1612:15:30"),
            name = name1,
            description = "desc_1",
            id = 3
        )
        val task2 = Task(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-02-1612:15:30"),
            name = name2,
            description = "desc_2",
            id = 3
        )
        val task3 = Task(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1612:15:30"),
            name = name3,
            description = "desc_3",
            id = 2
        )

        historyList.pushTask(task1)
        historyList.pushTask(task2)
        historyList.pushTask(task3)

        val returnedList = historyList.getList()

        assertNotNull(returnedList)

        assertTrue(returnedList[2] == task1)
        assertTrue(returnedList[1] == task2)
        assertTrue(returnedList[0] == task3)

    }

    @Test
    fun addAndDeleteAndGet_onDelete_taskAddedAndDeleted(){
        val historyList = HistoryList<Task> ()
        val name1 = "test_name_1"
        val name2 = "test_name_2"
        val name3 = "test_name_3"

        val task1 = Task(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-01-1612:15:30"),
            name = name1,
            description = "desc_1",
            id = 3
        )
        val task2 = Task(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-02-1612:15:30"),
            name = name2,
            description = "desc_2",
            id = 3
        )
        val task3 = Task(
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1612:15:30"),
            name = name3,
            description = "desc_3",
            id = 2
        )

        historyList.pushTask(task1)
        historyList.pushTask(task2)
        historyList.pushTask(task3)

        historyList.deleteTask(historyList.getTaskByID(1))

        val returnedTask1 = historyList.getTaskByID(0)
        val returnedTask2 = historyList.getTaskByID(1)
        val returnedTask3 = historyList.getTaskByID(2)

        assertNotNull(returnedTask1)
        assertNotNull(returnedTask2)

        assertTrue(returnedTask2 == task1)
        assertTrue(returnedTask2 == task2)
        assertTrue(returnedTask3 == null)
    }

    @Test
    fun deleteUntilAddGet1_onDeleteUntil_tasksDeletedUntilDate() {
        val historyList = HistoryList<Task> ()

        val taskList = mutableListOf<Task>()
        val startingDate = 1940
        val untilDate = 1960
        val range = 100
        for(i in 1..range){
            taskList.add(Task(
                dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("${startingDate + i}-03-1612:15:30"),
                name = "test_name$i",
                description = "desc_$i",
                id = 2
            ))
            historyList.pushTask(taskList.last())
        }

        historyList.deleteUntil(SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("$untilDate-03-1612:15:30"))

        val lastTask = historyList.getTaskByID(range - untilDate + startingDate)

        assertNotNull(lastTask)
        assertTrue(lastTask.it.dateOfCreation == SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("$untilDate-03-1612:15:30"))

    }

}