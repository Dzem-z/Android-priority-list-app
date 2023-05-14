package com.example.prioritylist.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.prioritylist.data.MainPage
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.prioritylist.data.PriorityTask
import java.time.LocalDateTime


@Composable
fun MainPageScreen(holder: StateHolder = StateHolder(), modifier: Modifier = Modifier) {



    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()

    NavHost(
        navController = navController,
        startDestination = "ListContainer",
        modifier = modifier
    ){
        composable(route = "ListContainer"){
            ListContainer(holder = holder, onAddTask = {
                holder.onAddTask()
                navController.navigate("AddTask")
            })
        }

        composable(route = "AddTask"){
            EditTaskScreen(
                holder = holder,
                onConfirmMessage = "add task",
                onConfirm = {
                    holder.addTask()
                    navController.popBackStack()
                }
            )
        }
    }




}

@Composable
fun TopBar(modifier: Modifier = Modifier) {

}

@Composable
@Preview
fun MainPagePreview() {
    MainPageScreen()
}