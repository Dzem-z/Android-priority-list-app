package com.example.prioritylist.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Spacer
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


@Composable
fun HistoryListContainer(
    modifier: Modifier = Modifier,
    holder: StateHolder = StateHolder(),
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

                    },
                    shape = RectangleShape
                ) {
                    Text(
                        holder.getName() + " history"
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



            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {

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
