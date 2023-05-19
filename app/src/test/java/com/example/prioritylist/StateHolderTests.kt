package com.example.prioritylist

import com.example.prioritylist.data.PriorityTask
import com.example.prioritylist.data.TaskTypes
import com.example.prioritylist.ui.StateHolder
import junit.framework.TestCase.assertTrue
import org.junit.Test
import java.text.SimpleDateFormat
import java.time.LocalDateTime

class StateHolderTests {

    @Test
    fun getList_listReadProperly() {
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
            dateOfCreation =  SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse("2023-03-1212:15:30"),
            name = "test_name_3",
            description = "desc_1",
            id = 3,
            priority = 1
        )

        val holder = StateHolder()


        holder.setList(mutableListOf<PriorityTask>(task1, task2, task3))

        assertTrue(holder.getCurrentType() == TaskTypes.PRIORITY)
        assertTrue(holder.getList()[0] == task1 && holder.getList()[1] == task2 && holder.getList()[2] == task3)

    }




}