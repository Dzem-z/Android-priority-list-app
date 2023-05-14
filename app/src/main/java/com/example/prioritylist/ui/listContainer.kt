package com.example.prioritylist.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.OutlinedButton
import androidx.compose.material.TextButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Menu
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
fun ListContainer( modifier: Modifier = Modifier, holder: StateHolder = StateHolder(), onAddTask: () -> Unit = {}) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    //val currentScreen
    Scaffold(
        topBar = {
            TopAppBar(
                title = { /*TODO*/ },
                navigationIcon = {
                    IconButton(
                        onClick = { /*TODO*/ }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Menu,
                            contentDescription = "Drawer Icon"
                        )
                    }
                },
                actions = {
                    /*TODO*/
                }
            )


        },

        floatingActionButton = {
            FloatingActionButton(onClick = onAddTask) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Drawer Icon"
                )
            }
        }

    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Row(
                Modifier.padding(0.dp),
            ) {
                TextButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    onClick = {/*TODO*/ },

                    shape = RectangleShape
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowBack,
                        contentDescription = "Drawer Icon"
                    )
                }

                TextButton(
                    modifier = Modifier
                        .weight(2f)
                        .height(48.dp),
                    onClick = {/*TODO*/ },
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
                    onClick = {/*TODO*/ },
                    shape = RectangleShape
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowForward,
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
                PriorityList(
                    holder
                )
            }

            composable(route = "DeadlineTask"){

            }
        }
        }
    }

}

@Preview
@Composable
fun ListContainerPreview() {
    ListContainer()
}
