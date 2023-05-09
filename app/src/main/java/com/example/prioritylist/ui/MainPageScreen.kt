package com.example.prioritylist.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.prioritylist.data.MainPage
import androidx.compose.ui.Modifier



@Composable
fun MainPageScreen(modifier: Modifier = Modifier) {
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


        }
    ){ innerPadding ->
        ListContainer(modifier = modifier.padding(innerPadding))

    }
}

@Composable
fun TopBar(modifier: Modifier = Modifier) {

}

@Composable
@Preview
fun MainPagePreview() {
    MainPageScreen()
}