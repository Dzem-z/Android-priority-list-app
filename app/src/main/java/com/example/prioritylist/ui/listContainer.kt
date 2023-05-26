package com.example.prioritylist.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
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
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.ModalBottomSheetLayout
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
    onRemoveList: () -> Unit = {}
    ) {


    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded },
        skipHalfExpanded = true
    )

    val roundedCornerRadius = 12.dp



    BackHandler(modalSheetState.isVisible) {
        coroutineScope.launch { modalSheetState.hide() }
    }


    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetShape = RoundedCornerShape(topStart = roundedCornerRadius, topEnd = roundedCornerRadius),
        sheetContent = if (holder.taskBottomSheetExpanded) {
            { TaskOptionsSheet(
                hide = { coroutineScope.launch { modalSheetState.hide() } },
                onEditTask = onEditTask,
                onDeleteTask = onDeleteTask
            ) }

        } else {
            { ListOptionsSheet(
                viewModel = holder,
                removeList = {
                    onRemoveList()
                    coroutineScope.launch { modalSheetState.hide() }
                },
                addList = {
                    coroutineScope.launch { modalSheetState.hide() }
                    onAddList()
                }
            ) }
            }

    ) {

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
                    if (holder.isPrevList) {
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
                            holder.taskBottomSheetExpanded = false
                            coroutineScope.launch {
                                modalSheetState.show()
                            }
                                  },
                        shape = RectangleShape
                    ) {
                        Text(
                            holder.getName()
                        )
                    }

                    if (holder.isNextList) {
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

                AnimatedVisibility(visible = holder.visible) {
                    if (holder.firstType == TaskTypes.PRIORITY) {
                        PriorityList(
                            holder,
                            holder.firstList as MutableList<PriorityTask>,
                        {
                            holder.taskBottomSheetExpanded = true
                            coroutineScope.launch {
                                modalSheetState.show()
                            }
                        })
                    } else if (holder.firstType == TaskTypes.DEADLINE) {
                        DeadlineList(
                            holder,
                            holder.firstList as MutableList<DeadlineTask>,
                        {
                            holder.taskBottomSheetExpanded = true
                            coroutineScope.launch {
                                modalSheetState.show()
                            }
                        })
                    }
                }

                AnimatedVisibility(visible = !holder.visible) {
                    if (holder.secondType == TaskTypes.PRIORITY) {
                        PriorityList(
                            holder,
                            holder.secondList as MutableList<PriorityTask>,
                        {
                            holder.taskBottomSheetExpanded = true
                            coroutineScope.launch {
                                modalSheetState.show()
                            }
                        })
                    } else if (holder.secondType == TaskTypes.DEADLINE) {
                        DeadlineList(
                            holder,
                            holder.secondList as MutableList<DeadlineTask>,
                        {
                            holder.taskBottomSheetExpanded = true
                            coroutineScope.launch {
                                modalSheetState.show()
                            }
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
