package com.example.prioritylist.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.prioritylist.data.MainPage
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController


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


        },

        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Drawer Icon"
                )
            }
        }

    ){ innerPadding ->
        val navController = rememberNavController()
        val backStackEntry by navController.currentBackStackEntryAsState()

        NavHost(
            navController = navController,
            startDestination = "ListContainer",
            modifier = modifier
        ){
            composable(route = "ListContainer"){
                ListContainer(modifier = modifier.padding(innerPadding))
            }

            composable(route = "AddTask"){

            }
        }



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