package com.example.prioritylist.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.OutlinedButton
import androidx.compose.material.TextButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.prioritylist.data.PriorityTask
import com.example.prioritylist.data.TaskTypes
import java.time.LocalDateTime

@Composable
fun ListContainer(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    //val currentScreen

    Column{
        Row(
            Modifier.padding(0.dp),
        ){
            TextButton(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                onClick = {/*TODO*/},

                shape = RectangleShape
            ) {
                Icon(
                    imageVector =  Icons.Outlined.ArrowBack,
                    contentDescription = "Drawer Icon"
                )
            }

            TextButton(
                modifier = Modifier
                    .weight(2f)
                    .height(48.dp),
                onClick = {/*TODO*/},
                shape = RectangleShape
            ) {
                Text(
                    "screen 1"
                )
            }

            TextButton(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                onClick = {/*TODO*/},
                shape = RectangleShape
            ) {
                Icon(
                    imageVector =  Icons.Outlined.ArrowForward,
                    contentDescription = "Drawer Icon"
                )
            }

        }
        NavHost(
            navController = navController,
            startDestination = "PriorityTask",
            modifier = modifier
        ){
            composable(route = "PriorityTask"){
                //-------------------------testing values
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

                PriorityList(
                    StateHolder(
                        currentType = TaskTypes.PRIORITY,
                        displayingList = listOf<PriorityTask>(
                            task1, task2, task3, task4, task5, task6, task7, task8, task9
                        )
                    )
                )
                //-------------------------testing values end
            }

            composable(route = "DeadlineTask"){

            }
        }
    }


}

@Preview
@Composable
fun ListContainerPreview() {
    ListContainer()
}
