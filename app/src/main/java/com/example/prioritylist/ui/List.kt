package com.example.prioritylist.ui

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.prioritylist.data.PriorityTask
import com.example.prioritylist.data.TaskTypes
import java.time.LocalDateTime

@Composable
fun PriorityList(viewModel: StateHolder, modifier: Modifier = Modifier) {

        //val list = viewModel.displayingList as List<PriorityTask>

        LazyColumn(

        ){
            items(
                viewModel.displayingList
            ) {item->
                PriorityTaskTile(
                    tile = item as PriorityTask,
                    modifier = Modifier.pointerInput(item) {
                        detectTapGestures(
                            onPress = { /* Called when the gesture starts */ },
                            onDoubleTap = { /**/ },
                            onLongPress = { viewModel.onDelete(item) },
                            onTap = { /* Called on Tap */ }
                        )
                    }
                )
            }
        }







}

@Composable
fun PriorityTaskTile(tile: PriorityTask, modifier: Modifier = Modifier) {
    Card(
        elevation = 10.dp,
        modifier = modifier
            .padding(10.dp)
            .fillMaxWidth()
    ){
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(text = tile.name)

            Row{
              Text(text = "priority: " + tile.priority)
            }

        }
    }
}

@Composable
@Preview
fun ListPreview() {

    val holder = StateHolder()

    PriorityList(
        holder
    )
}