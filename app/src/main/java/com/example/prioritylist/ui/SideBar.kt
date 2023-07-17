package com.example.prioritylist.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * [SideBar] is an composable that is displayed as a sidebar
 * @param goToHome is called when user clicks text: home
 * @param goToHistory is called when user clicks text: history
 * @param goToSettings is called when user clicks text: settings
 * @param goToHelp is called when user clicks text: help
 * */

@Composable
fun SideBar(
    modifier: Modifier = Modifier,
    goToHome: () -> Unit = {},
    goToHistory: () -> Unit = {},
    goToSettings: () -> Unit = {},
    goToHelp: () -> Unit = {}
) {
    Column(
        modifier
            .fillMaxSize()
            .padding(start = 24.dp, end = 24.dp, top = 48.dp, bottom = 48.dp)
    ) {
        Text(
            "home",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clickable { goToHome() }
        )
        Text(
            "history",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clickable { goToHistory() }
        )
        Text(
            "settings",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clickable { goToSettings() }
        )
        Text(
            "help",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clickable { goToHelp() }
        )
        Spacer(modifier = Modifier.weight(3f))
    }
}

@Preview(showBackground = true)
@Composable
fun SideBarPreview(){
    SideBar()
}
