package com.example.prioritylist

import com.example.prioritylist.data.HistoryList
import com.example.prioritylist.data.HistoryTask
import com.example.prioritylist.data.PriorityTask
import com.example.prioritylist.data.Task
import junit.framework.TestCase.assertNotNull
import org.junit.Assert.assertThrows
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

        val historyTask1 = HistoryTask(task1, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-04-1612:15:30"))
        val historyTask2 = HistoryTask(task2, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1612:15:30"))
        val historyTask3 = HistoryTask(task3, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-02-1612:15:30"))

        historyList.pushTask(task3, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-02-1612:15:30"))
        historyList.pushTask(task1, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-04-1612:15:30"))
        historyList.pushTask(task2, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1612:15:30"))

        val returnedTask1 = historyList.getTaskByPos(0)
        val returnedTask2 = historyList.getTaskByPos(1)
        val returnedTask3 = historyList.getTaskByPos(2)

        assertThrows(IndexOutOfBoundsException::class.java){
            val returnedTask4 = historyList.getTaskByPos(10)
        }



        assertNotNull(returnedTask1)
        assertNotNull(returnedTask2)
        assertNotNull(returnedTask3)

        assertTrue(returnedTask3 == historyTask3)
        assertTrue(returnedTask2 == historyTask2)
        assertTrue(returnedTask1 == historyTask1)
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

        val historyTask1 = HistoryTask(task1, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-02-1612:15:30"))
        val historyTask2 = HistoryTask(task2, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1612:15:30"))
        val historyTask3 = HistoryTask(task3, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-01-1612:15:30"))

        historyList.pushTask(task1, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-02-1612:15:30"))
        historyList.pushTask(task2, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1612:15:30"))
        historyList.pushTask(task3, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-01-1612:15:30"))

        val returnedList = historyList.getList()

        assertNotNull(returnedList)

        assertTrue(returnedList[2] == historyTask3)
        assertTrue(returnedList[1] == historyTask1)
        assertTrue(returnedList[0] == historyTask2)

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

        val historyTask1 = HistoryTask(task1, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1612:15:30"))
        val historyTask2 = HistoryTask(task2, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-01-1612:15:30"))

        historyList.pushTask(task1, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1612:15:30"))
        historyList.pushTask(task2, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-01-1612:15:30"))
        historyList.pushTask(task3, SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-02-1612:15:30"))

        historyList.deleteTask(historyList.getTaskByPos(1))

        val returnedTask1 = historyList.getTaskByPos(0)
        val returnedTask2 = historyList.getTaskByPos(1)

        assertThrows(IndexOutOfBoundsException::class.java) {
            val returnedTask3 = historyList.getTaskByPos(2)
        }

        assertNotNull(returnedTask1)
        assertNotNull(returnedTask2)

        assertTrue(returnedTask1 == historyTask1)
        assertTrue(returnedTask2 == historyTask2)
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
            historyList.pushTask(taskList.last(), SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("${startingDate + i}-03-1612:15:30"))
        }

        historyList.deleteUntil(SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("$untilDate-03-1612:15:30"))

        val lastTask = historyList.getTaskByPos(range - untilDate + startingDate)

        assertNotNull(lastTask)
        assertTrue(lastTask.it.dateOfCreation == SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("$untilDate-03-1612:15:30"))

    }

}