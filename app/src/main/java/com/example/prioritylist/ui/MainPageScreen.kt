package com.example.prioritylist.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.prioritylist.data.StatusCodes


val springSpec = spring<IntOffset>(dampingRatio = Spring.DampingRatioMediumBouncy)

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainPageScreen(holder: StateHolder = StateHolder(), modifier: Modifier = Modifier) {



    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()

    NavHost(
        navController = navController,
        startDestination = "ListContainer",
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
                    holder.onDeleteTask()
                },
                onAddList = {
                    navController.navigate("AddList")
                },
                onRemoveList = {
                    holder.removeList()
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
                    val code = holder.addTask().code
                    if (code == StatusCodes.DUPLICATED_TASK) {
                        holder.setDuplicatedTaskError()
                    } else if (code == StatusCodes.EMPTY_NAME){
                        holder.setEmptyNameError()
                    }
                    else {
                        holder.resetEditedTask()
                        navController.popBackStack()
                    }
                            },
                onCancel = {
                    holder.resetEditedTask()
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
                    holder.confirmEditingTask()
                    holder.resetEditedTask()
                    navController.popBackStack()
                            },
                onCancel = {
                    holder.resetEditedTask()
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = "AddList"
        ) {
            AddListScreen(
                holder = holder,
                onCancel = {
                    holder.resetAddListParameters()
                    navController.popBackStack()
                },
                onConfirm = {
                    holder.addList()
                    navController.popBackStack()
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