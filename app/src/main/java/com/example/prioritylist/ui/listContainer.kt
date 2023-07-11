package com.example.prioritylist.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.OutlinedButton
import androidx.compose.material.TextButton
import androidx.compose.material.Icon
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.AlertDialog
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prioritylist.R
import com.example.prioritylist.data.backend.DeadlineTask
import com.example.prioritylist.data.backend.PriorityTask
import com.example.prioritylist.data.backend.TaskTypes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime

/**
* [ListContainer] is a composable that displays list information
* @param holder an instance of StateHolder recieved from viewModel factory
* @param onAddTask is called when user taps add task button
* @param onEditTaks is called when user taps edit task button
* @param onDeleteTask is called when user deletes task
* @param onAddList is called when user taps add list button
* @param onRemoveList is called when user removes list
* @param onGoToHistory is called when user navigates to history
* @param globalScope in with database i/o is executed
* */

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListContainer(
    modifier: Modifier = Modifier,
    holder: StateHolder = viewModel(factory = AppViewModelProvider.Factory),
    onAddTask: () -> Unit = {},
    onEditTask: () -> Unit = {},
    onDeleteTask: () -> Unit = {},
    onAddList:() -> Unit = {},
    onRemoveList: () -> Unit = {},
    onGoToHistory: () -> Unit = {},
    globalScope: CoroutineScope = rememberCoroutineScope()
    ) {

    val coroutineScope = rememberCoroutineScope()   //an inner coroutineScope
    val snackbarPosition = remember { Animatable(180f) }    //the vertical position of the snackbar
    var isDialogOpened by remember { mutableStateOf(false) } //state flag indicating if undo dialog is opened

    val scaffoldState = rememberScaffoldState()
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded },
        skipHalfExpanded = true
    )


    LaunchedEffect(modalSheetState.isVisible){
        coroutineScope.launch{
            snackbarPosition.animateTo(if (modalSheetState.isVisible) 180f else 0f)
        }
    }

    val roundedCornerRadius = 12.dp //used in [modalBottomSheetLayout]



    BackHandler(modalSheetState.isVisible) {    //executed when user presses back button
        coroutineScope.launch {
            modalSheetState.hide()
        }
    }


    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetShape = RoundedCornerShape(topStart = roundedCornerRadius, topEnd = roundedCornerRadius),
        sheetContent = if (holder.UI.taskBottomSheetExpanded) { //determines which bottomSheet should be expanded
            { TaskOptionsSheet(
                hide = {
                    coroutineScope.launch { modalSheetState.hide() }
                       },
                onEditTask = onEditTask,
                onDeleteTask = onDeleteTask
            ) }

        } else {
            { ListOptionsSheet(
                viewModel = holder,
                removeList = {
                    coroutineScope.launch { modalSheetState.hide() }
                    onRemoveList()
                },
                addList = {
                    coroutineScope.launch { modalSheetState.hide() }
                    onAddList()
                },
                goToHistory = {
                    coroutineScope.launch { modalSheetState.hide() }
                    onGoToHistory()
                },
                launchSnackbar = { message ->
                    coroutineScope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(message)
                    }
                },
                globalScope = globalScope    //global scope

            ) }
            }

    ) {

        Scaffold(
            scaffoldState = scaffoldState,

            snackbarHost = {
                // reuse default SnackbarHost to have default animation and timing handling
                SnackbarHost(it) { data ->
                    Snackbar(
                        modifier = Modifier.padding(vertical = snackbarPosition.value.dp),
                        snackbarData = data
                    )
                }
            },


            topBar = {
                TopAppBar(  //side bar button
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
                    actions = { //undo button, usable whenever there is an action in the storage
                        IconButton(
                            onClick = { isDialogOpened = true },
                            enabled = !holder.Read.isStorageEmpty
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.undo_48px),
                                tint = if (!holder.Read.isStorageEmpty) Color.White else Color.Transparent,
                                modifier = Modifier.scale(0.85f),
                                contentDescription = "Drawer Icon"
                            )
                        }
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

            if (isDialogOpened) {
                AlertDialog(
                    onDismissRequest = {

                    },
                    title = {
                        Text(text = "Do you want to undo previous action?")
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                globalScope.launch { holder.onUndo() }
                                isDialogOpened = false
                            }) {
                            Text(text = "undo")
                        }
                    },
                    dismissButton = {
                        OutlinedButton(
                            onClick = {
                                isDialogOpened = false
                            }) {
                            Text(text = "cancel")
                        }
                    }
                )
            }

            Column(modifier = Modifier.padding(innerPadding)) {
                Row(
                    Modifier.padding(0.dp),
                ) {
                    if (holder.Read.isPrevList) {  //previous list navigator
                        TextButton(
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            onClick = {
                                holder.prevList()
                            },

                            shape = RectangleShape
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ArrowBack,
                                contentDescription = "Drawer Icon"
                            )
                        }
                    } else {
                        Spacer(
                            modifier = Modifier
                                .weight(1f)
                        )
                    }


                    TextButton( //list button
                        modifier = Modifier
                            .weight(2f)
                            .height(48.dp),
                        onClick = {
                            holder.UI.taskBottomSheetExpanded = false
                            coroutineScope.launch { modalSheetState.show() }
                                  },
                        shape = RectangleShape
                    ) {
                        Text(
                            holder.Read.getName()
                        )
                    }

                    if (holder.Read.isNextList) {   //next list navigator
                        TextButton(
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            onClick = {
                                holder.nextList()
                            },
                            shape = RectangleShape
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ArrowForward,
                                contentDescription = "Drawer Icon"
                            )
                        }
                    } else {
                        Spacer(
                            modifier = Modifier
                                .weight(1f)
                        )
                    }

                }
                /*
                * when user navigates to the next list, states change and animation is launched
                * */
                AnimatedVisibility(visible = holder.UI.visible) {
                    if (holder.Read.firstType == TaskTypes.PRIORITY) {
                        PriorityList(
                            holder,
                            holder.Read.firstList as MutableList<PriorityTask>,
                            globalScope,
                        {
                            holder.UI.taskBottomSheetExpanded = true
                            coroutineScope.launch { modalSheetState.show() }
                        })
                    } else if (holder.Read.firstType == TaskTypes.DEADLINE) {
                        DeadlineList(
                            holder,
                            holder.Read.firstList as MutableList<DeadlineTask>,
                            globalScope,
                        {
                            holder.UI.taskBottomSheetExpanded = true
                            coroutineScope.launch { modalSheetState.show() }
                        })
                    }
                }

                AnimatedVisibility(visible = !holder.UI.visible) {
                    if (holder.Read.secondType == TaskTypes.PRIORITY) {
                        PriorityList(
                            holder,
                            holder.Read.secondList as MutableList<PriorityTask>,
                            globalScope,
                        {
                            holder.UI.taskBottomSheetExpanded = true
                            coroutineScope.launch { modalSheetState.show() }
                        })
                    } else if (holder.Read.secondType == TaskTypes.DEADLINE) {
                        DeadlineList(
                            holder,
                            holder.Read.secondList as MutableList<DeadlineTask>,
                            globalScope,
                        {
                            holder.UI.taskBottomSheetExpanded = true
                            coroutineScope.launch { modalSheetState.show() }
                        })
                    }
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
