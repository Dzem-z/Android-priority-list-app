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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.prioritylist.R
import com.example.prioritylist.data.DeadlineTask
import com.example.prioritylist.data.PriorityTask
import com.example.prioritylist.data.TaskTypes
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListContainer(
    modifier: Modifier = Modifier,
    holder: StateHolder = StateHolder(),
    onAddTask: () -> Unit = {},
    onEditTask: () -> Unit = {},
    onDeleteTask: () -> Unit = {},
    onAddList:() -> Unit = {},
    onRemoveList: () -> Unit = {},
    onGoToHistory: () -> Unit = {}
    ) {

    val snackbarPosition = remember { Animatable(180f) }
    val coroutineScope = rememberCoroutineScope()

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

    val roundedCornerRadius = 12.dp



    BackHandler(modalSheetState.isVisible) {
        coroutineScope.launch {
            modalSheetState.hide()
        }
    }


    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetShape = RoundedCornerShape(topStart = roundedCornerRadius, topEnd = roundedCornerRadius),
        sheetContent = if (holder.UI.taskBottomSheetExpanded) {
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
                }

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
                        IconButton(
                            onClick = { holder.onUndo() },
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
            Column(modifier = Modifier.padding(innerPadding)) {
                Row(
                    Modifier.padding(0.dp),
                ) {
                    if (holder.Read.isPrevList) {
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


                    TextButton(
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

                    if (holder.Read.isNextList) {
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

                AnimatedVisibility(visible = holder.UI.visible) {
                    if (holder.Read.firstType == TaskTypes.PRIORITY) {
                        PriorityList(
                            holder,
                            holder.Read.firstList as MutableList<PriorityTask>,
                        {
                            holder.UI.taskBottomSheetExpanded = true
                            coroutineScope.launch { modalSheetState.show() }
                        })
                    } else if (holder.Read.firstType == TaskTypes.DEADLINE) {
                        DeadlineList(
                            holder,
                            holder.Read.firstList as MutableList<DeadlineTask>,
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
                        {
                            holder.UI.taskBottomSheetExpanded = true
                            coroutineScope.launch { modalSheetState.show() }
                        })
                    } else if (holder.Read.secondType == TaskTypes.DEADLINE) {
                        DeadlineList(
                            holder,
                            holder.Read.secondList as MutableList<DeadlineTask>,
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
