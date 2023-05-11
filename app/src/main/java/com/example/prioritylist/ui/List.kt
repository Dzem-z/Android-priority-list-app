package com.example.prioritylist.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.prioritylist.data.PriorityTask
import com.example.prioritylist.data.TaskTypes
import java.time.LocalDateTime

@Composable
fun PriorityList(viewModel: StateHolder, modifier: Modifier = Modifier) {
    val list = viewModel.displayingList as List<PriorityTask>

    LazyColumn(
    ){
        items(list) {
            PriorityTaskTile(tile = it)
        }
    }

}

@Composable
fun PriorityTaskTile(tile: PriorityTask) {
    Card(
        elevation = 10.dp,
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
    ){
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(text = tile.name)

            Row{
              Text(text = "priority: " + tile.priority)
            }

        }
    }
}

@Composable
@Preview
fun ListPreview() {
    val task1 = PriorityTask(
        dateOfCreation =  LocalDateTime.parse("2023-03-12T12:15:30"),
        name = "test_name_1",
        description = "desc_1",
        id = 3,
        priority = 3
    )
    val task2 = PriorityTask(
        dateOfCreation =  LocalDateTime.parse("2023-03-24T12:15:30"),
        name = "test_name_2",
        description = "desc_2",
        id = 3,
        priority = 2
    )
    val task3 = PriorityTask(
        dateOfCreation =  LocalDateTime.parse("2023-03-12T12:15:30"),
        name = "test_name_3",
        description = "desc_1",
        id = 3,
        priority = 1
    )
    val task4 = PriorityTask(
        dateOfCreation =  LocalDateTime.parse("2023-03-12T12:15:30"),
        name = "test_name_1",
        description = "desc_1",
        id = 3,
        priority = 3
    )
    val task5 = PriorityTask(
        dateOfCreation =  LocalDateTime.parse("2023-03-24T12:15:30"),
        name = "test_name_2",
        description = "desc_2",
        id = 3,
        priority = 2
    )
    val task6 = PriorityTask(
        dateOfCreation =  LocalDateTime.parse("2023-03-12T12:15:30"),
        name = "test_name_3",
        description = "desc_1",
        id = 3,
        priority = 1
    )
    val task7 = PriorityTask(
        dateOfCreation =  LocalDateTime.parse("2023-03-12T12:15:30"),
        name = "test_name_1",
        description = "desc_1",
        id = 3,
        priority = 3
    )
    val task8 = PriorityTask(
        dateOfCreation =  LocalDateTime.parse("2023-03-24T12:15:30"),
        name = "test_name_2",
        description = "desc_2",
        id = 3,
        priority = 2
    )
    val task9 = PriorityTask(
        dateOfCreation =  LocalDateTime.parse("2023-03-12T12:15:30"),
        name = "test_name_3",
        description = "desc_1",
        id = 3,
        priority = 1
    )

    val holder = StateHolder()
    holder.displayingList.value = mutableListOf<PriorityTask>(
        task1, task2, task3, task4, task5, task6, task7, task8, task9
    )

    PriorityList(
        holder
    )
}