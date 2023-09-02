package com.example.prioritylist.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.TextButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Text
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarColors
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prioritylist.ui.theme.PriorityListTheme
import com.example.prioritylist.ui.theme.ThemeID

/**
* [HistoryListContainer] is a composable with functionality analogous to the listContainer
* @param holder is an instance of stateHolder initialized with viewModel factory
* @param displaySidebar is called when user opens sidebar (clicks navigation button located on the sidebar
* */


@Composable
fun HistoryListContainer(
    modifier: Modifier = Modifier,
    holder: StateHolder = viewModel(factory = AppViewModelProvider.Factory),
    displaySidebar: () -> Unit = {}
) {

    Scaffold(

        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colorScheme.onPrimary,
                title = { /*TODO*/ },
                navigationIcon = {
                    IconButton(
                        onClick = { displaySidebar() }
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


        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Row(
                Modifier.padding(0.dp)
            ) {
                if (holder.Read.isPrevList) {  //previous list navigator
                    Box(modifier = Modifier
                        .weight(1f)
                        .padding(horizontal=20.dp)
                    ) {
                        this@Row.AnimatedVisibility(
                            //next list navigator
                            visible = holder.Read.isPrevList && holder.UI.firstVisibleState.isIdle && holder.UI.secondVisibleState.isIdle,
                        ) {
                            TextButton(
                                modifier = Modifier
                                    .height(48.dp),
                                onClick = {
                                    holder.prevList()
                                },

                                shape = RectangleShape
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.ArrowBack,
                                    tint = MaterialTheme.colorScheme.secondary,
                                    contentDescription = "Drawer Icon"
                                )
                            }
                        }
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

                    },
                    shape = RectangleShape
                ) {
                    Text(
                        text = holder.Read.getName() + " history",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                if (holder.Read.isNextList) {
                    Box(modifier = Modifier
                        .weight(1f)
                        .padding(horizontal=20.dp)
                    ) {
                        this@Row.AnimatedVisibility( //next list navigator
                            visible = holder.Read.isNextList && holder.UI.firstVisibleState.isIdle && holder.UI.secondVisibleState.isIdle,
                        ) {
                            TextButton(
                                modifier = Modifier
                                    .height(48.dp),
                                onClick = {
                                    holder.nextList()
                                },
                                shape = RectangleShape
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.ArrowForward,
                                    tint = MaterialTheme.colorScheme.secondary,
                                    contentDescription = "Drawer Icon"
                                )
                            }
                        }
                    }
                } else {
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                    )
                }

            }

            Column(modifier = modifier.weight(1f)) {


                if (!holder.UI.visible && holder.UI.isAnimationPending) {  //continues animation: triggers animation of the invisible AnimatedVisibility after first animation finishes
                    if (holder.UI.firstVisibleState.isIdle && !holder.UI.firstVisibleState.currentState) {
                        holder.UI.secondVisibleState.targetState =
                            !holder.UI.secondVisibleState.targetState
                        holder.UI.isAnimationPending = false
                    }
                }
                if (holder.UI.visible && holder.UI.isAnimationPending) {   //continues animation: triggers animation of the invisible AnimatedVisibility after first animation finishes
                    if (holder.UI.secondVisibleState.isIdle && !holder.UI.secondVisibleState.currentState) {
                        holder.UI.firstVisibleState.targetState =
                            !holder.UI.firstVisibleState.targetState
                        holder.UI.isAnimationPending = false
                    }
                }
                /*
                * when user navigates to the next list, states change and animation is launched
                * */
                AnimatedVisibility(
                    visibleState = holder.UI.firstVisibleState,
                    enter = if (holder.UI.isLeftSwipe) {
                        slideInHorizontally(
                            animationSpec = holder.UI.animationSpecSpring,
                            initialOffsetX = { -it / 2 }
                        )
                    } else {
                        slideInHorizontally(
                            animationSpec = holder.UI.animationSpecSpring,
                            initialOffsetX = { it / 2 }
                        )
                    },
                    exit = if (holder.UI.isLeftSwipe) {
                        slideOutHorizontally(
                            //animationSpec = animationSpecSpring,
                            targetOffsetX =  { it }
                        )
                    } else {
                        slideOutHorizontally(
                            //animationSpec = animationSpecSpring,
                            targetOffsetX = { -it }
                        )
                    }
                ) {
                   HistoryList(
                       viewModel = holder,
                       list = holder.Read.firstHistoryList,
                       type = holder.Read.firstType
                   )
                }

                AnimatedVisibility(
                    visibleState = holder.UI.secondVisibleState,
                    enter = if (holder.UI.isLeftSwipe) {
                        slideInHorizontally(
                            animationSpec = holder.UI.animationSpecSpring,
                            initialOffsetX = { -it / 2 }
                        )
                    } else {
                        slideInHorizontally(
                            animationSpec = holder.UI.animationSpecSpring,
                            initialOffsetX = { it / 2 }
                        )
                    },
                    exit = if (holder.UI.isLeftSwipe) {
                        slideOutHorizontally(
                            //animationSpec = animationSpecSpring,
                            targetOffsetX =  { it }
                        )
                    } else {
                        slideOutHorizontally(
                            //animationSpec = animationSpecSpring,
                            targetOffsetX = { -it }
                        )
                    }
                ) {
                    HistoryList(
                        viewModel = holder,
                        list = holder.Read.secondHistoryList,
                        type = holder.Read.secondType
                    )
                }
            }
        }


    }
}

@Preview(showBackground = true)
@Composable
fun HistoryListContainerPreview() {
    PriorityListTheme(themeId = ThemeID.FIRST) {
        HistoryListContainer()
    }
}
