package com.example.prioritylist.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.prioritylist.data.backend.StatusCodes
import kotlinx.coroutines.launch




/**
* [MainPageScreen] is a composable responsible for navigating between screens, it is an entry point for every composable
* @param holder an instance of StateHolder recieved from viewModel factory
*  */
@Composable
fun MainPageScreen(
    holder: StateHolder = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier) {


    val coroutineScope = rememberCoroutineScope()   //an global coroutineScope in which all database actions are done
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = if (holder.Read.isEmpty()) "EmptyScreen" else "ListContainer",   //if list is empty, navigates to the empty screen, listContainer otherwise
        modifier = modifier
    ){
        composable(
            route = "ListContainer"
        ) {
            ListContainer(holder = holder,
                onAddTask = {   //when user taps add task button
                    holder.onAddTask()
                    navController.navigate("AddTask")
                            },
                onEditTask = {  //when user taps edit task button
                    holder.onEditTask()
                    navController.navigate("EditTask")
                },
                onDeleteTask = { //when user deletes task
                    coroutineScope.launch {
                        holder.onDeleteTask()
                    }
                },
                onAddList = {   //when user taps add list button
                    navController.navigate("AddList")
                },
                onRemoveList = {    //when user removes list

                    if(holder.Read.isAlmostEmpty()) //if no lists left then navigate to empty screen
                        navController.navigate("EmptyScreen"){
                            popUpTo(0)
                        }
                    coroutineScope.launch {
                        holder.removeList()
                    }
                },
                onGoToHistory = {
                    navController.navigate("historyScreen")
                }
            )
        }

        composable(
            route = "AddTask"
        ) {
            EditTaskScreen(
                holder = holder,
                onConfirmMessage = "add task",
                onConfirm = {   //an confirming action
                    coroutineScope.launch {
                        val code = holder.addTask().code
                        if (code == StatusCodes.DUPLICATED_TASK) {
                            holder.UI.setDuplicatedTaskError()
                        } else if (code == StatusCodes.EMPTY_NAME) {
                            holder.UI.setEmptyNameError()
                        } else {
                            holder.UI.resetEditedTask()
                            navController.popBackStack()
                        }
                    }
                            },
                onCancel = {    //when user cancels adding task
                    holder.UI.resetEditedTask()
                    navController.popBackStack()
                }
            )

        }

        composable(
            route = "EditTask"
        ) {
            EditTaskScreen(
                holder = holder,
                onConfirmMessage = "confirm",
                onConfirm = {   //an confirming action
                    coroutineScope.launch {
                        holder.confirmEditingTask()
                        holder.UI.resetEditedTask()
                        navController.popBackStack()
                    }
                            },
                onCancel = { //when user cancels adding task
                    holder.UI.resetEditedTask()
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = "AddList"
        ) {
            AddListScreen(
                UIholder = holder.UI,
                onCancel = { //when user cancels adding list
                    holder.UI.resetAddListParameters()
                    navController.popBackStack()
                },
                onConfirm = { //an confirming action
                    coroutineScope.launch {
                        holder.addList()
                    }

                    navController.navigate("ListContainer"){
                        popUpTo(0)
                    }
                    //navController.popBackStack()

                }
            )
        }

        composable(
            route = "historyScreen"
        ) {
            HistoryListContainer(
                holder = holder,
                goToListScreen = { navController.popBackStack() }
            )
        }

        composable(
            route = "EmptyScreen"
        ) {

            EmptyScreen(
                onAddList = {
                    navController.navigate("AddList")
                }
            )
        }

    }


}

@Preview
@Composable
fun MainPagePreview() {
    MainPageScreen()
}