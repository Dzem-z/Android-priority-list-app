package com.example.prioritylist.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TextButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Button
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.Icons
import androidx.compose.material.Text
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import com.example.prioritylist.data.DeadlineTask
import com.example.prioritylist.data.PriorityTask
import com.example.prioritylist.data.TaskTypes
import kotlinx.coroutines.launch


@Composable
fun HistoryListContainer(
    modifier: Modifier = Modifier,
    holder: StateHolder = StateHolder(),
    goToListScreen: () -> Unit = {}
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,

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


        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Row(
                Modifier.padding(0.dp)
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

                    },
                    shape = RectangleShape
                ) {
                    Text(
                        holder.Read.getName() + " history"
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

            Column(modifier = modifier.weight(1f)) {
                this@Column.AnimatedVisibility(
                    visible = holder.UI.visible
                ) {
                   HistoryList(
                       viewModel = holder,
                       list = holder.Read.firstHistoryList,
                       type = holder.Read.firstType
                   )
                }

                this@Column.AnimatedVisibility(
                    visible = !holder.UI.visible
                ) {
                    HistoryList(
                        viewModel = holder,
                        list = holder.Read.secondHistoryList,
                        type = holder.Read.secondType
                    )
                }
            }

            Button(
                onClick = { goToListScreen() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowBack,
                        contentDescription = "Drawer Icon"
                    )
                }
            }
        }


    }
}

@Preview(showBackground = true)
@Composable
fun HistoryListContainerPreview() {
    HistoryListContainer()
}
