package com.example.prioritylist.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.prioritylist.data.HistoryTask
import com.example.prioritylist.data.Task

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HistoryList(
    viewModel: StateHolder,
    list: MutableList<out HistoryTask<out Task>>,
    modifier: Modifier = Modifier,
    onDoubleTap: () -> Unit = {}
) {

    LazyColumn(
        modifier = modifier
            .padding(end = 10.dp)
            .fillMaxHeight()
    ) {
        items(
            list,
            key = { it.it.name }
        ) { item ->
            HistoryTaskTile(
                tile = item,
                modifier = Modifier
                    .animateItemPlacement()
                    .pointerInput(item) {
                        detectTapGestures(
                            onPress = {},
                            onDoubleTap = {},
                            onLongPress = {},
                            onTap = {}
                        )
                    }
            )
        }
    }
}

@Composable
fun HistoryTaskTile(tile: HistoryTask<out Task>, modifier: Modifier = Modifier) {
    Card(
        elevation = 5.dp,
        modifier = modifier
            .padding(10.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = modifier.padding(12.dp)
        ) {
            Text(text = tile.it.name)

            Row{
                Text(text = "")
            }

        }

    }
}

@Preview(showBackground = true)
@Composable
fun HistoryListPreview(){
    val viewModel = StateHolder()
    HistoryList(viewModel, viewModel.getHistoryList())
}
