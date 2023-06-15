package com.example.prioritylist.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.prioritylist.data.backend.StatusCodes
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


val springSpec = spring<IntOffset>(dampingRatio = Spring.DampingRatioMediumBouncy)

@Composable
fun MainPageScreen(
    holder: StateHolder = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier) {


    val coroutineScope = rememberCoroutineScope()
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = if (holder.Read.isEmpty()) "EmptyScreen" else "ListContainer",
        modifier = modifier
    ){
        composable(
            route = "ListContainer"
        ) {
            ListContainer(holder = holder,
                onAddTask = {
                    holder.onAddTask()
                    navController.navigate("AddTask")
                            },
                onEditTask = {
                    holder.onEditTask()
                    navController.navigate("EditTask")
                },
                onDeleteTask = {
                    coroutineScope.launch {
                        holder.onDeleteTask()
                    }
                },
                onAddList = {
                    navController.navigate("AddList")
                },
                onRemoveList = {

                    if(holder.Read.isAlmostEmpty())
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
                onConfirm = {
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
                onCancel = {
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
                onConfirm = {
                    coroutineScope.launch {
                        holder.confirmEditingTask()
                        holder.UI.resetEditedTask()
                        navController.popBackStack()
                    }
                            },
                onCancel = {
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
                onCancel = {
                    holder.UI.resetAddListParameters()
                    navController.popBackStack()
                },
                onConfirm = {
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
                    /*if (!holder.Read.isEmpty())
                        coroutineScope.launch {
                            holder.removeList()
                        }*/
                    navController.navigate("AddList")
                }
            )
        }

    }


}


@Composable
fun TopBar(modifier: Modifier = Modifier) {

}

@Preview
@Composable
fun MainPagePreview() {
    MainPageScreen()
}