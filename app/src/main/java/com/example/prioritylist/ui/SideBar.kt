package com.example.prioritylist.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text as Text3
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.prioritylist.R

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
        
        Image(
            painter = painterResource(id = R.drawable.prioritylist_icon_v2),
            contentDescription = "priority list icon",
            modifier = Modifier
                .fillMaxWidth()
                .weight(3f)
        )

        Spacer(modifier = Modifier.weight(1f))

        Text3(
            stringResource(id = R.string.home),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clickable { goToHome() }
        )
        Text3(
            stringResource(id = R.string.history),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clickable { goToHistory() }
        )
        Text3(
            stringResource(id = R.string.settings),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clickable { goToSettings() }
        )
        Text3(
            stringResource(id = R.string.help),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clickable { goToHelp() }
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview(showBackground = true)
@Composable
fun SideBarPreview(){
    SideBar()
}
