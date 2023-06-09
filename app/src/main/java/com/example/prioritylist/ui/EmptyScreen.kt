package com.example.prioritylist.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.rememberScaffoldState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
* [EmptyScreen] is a composable responsible for displaying the emptyScreen (when there is no list to display)
* @param onAddList is called when user presses add list button
* */


@Composable
fun EmptyScreen(
    modifier: Modifier = Modifier,
    onAddList:() -> Unit = {}
    ) {

    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,

        topBar = {
            TopAppBar(
                title = { /*TODO*/ }
            )


        }

    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Add your first list!")

                Spacer(modifier = Modifier.padding(8.dp))

                Button(
                    onClick = { onAddList() },
                    modifier = Modifier
                        .height(64.dp)
                        .width(196.dp)
                ) {
                    Text(text = "add list")
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyScreenComposable() {
    EmptyScreen()
}