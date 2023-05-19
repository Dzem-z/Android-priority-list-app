package com.example.prioritylist.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.prioritylist.data.StatusCodes
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController


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