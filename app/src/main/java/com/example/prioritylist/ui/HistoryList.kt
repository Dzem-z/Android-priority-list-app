package com.example.prioritylist.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prioritylist.data.backend.CategoryTask
import com.example.prioritylist.data.backend.DeadlineTask
import com.example.prioritylist.data.backend.HistoryTask
import com.example.prioritylist.data.backend.PriorityTask
import com.example.prioritylist.data.backend.Task
import com.example.prioritylist.data.backend.TaskTypes

/**
* [HistoryList] is an composable with functionality analogous to List composable
* @param viewModel is an instance of StateHolder
* @param list is an list of historyTasks of any type to be displayed
* @param type is an actual type of Tasks
* @param onDoubleTap called on double tap on the tile
* */

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HistoryList(
    viewModel: StateHolder,
    list: MutableList<out HistoryTask<out Task>>,
    type: TaskTypes,
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
                    },
                type = type
            )
        }
    }
}
/**
* [HistoryTaskTile] is a composable responsible for displaying history tasks tiles on the screen
* @param tile is an HistoryTask to be displayed
* @param type is an type of the task, is used to determine which parameters should be displayed (must match historyTask type!)
* */
@Composable
fun HistoryTaskTile(tile: HistoryTask<out Task>, modifier: Modifier = Modifier, type: TaskTypes) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        elevation = 0.dp,
        modifier = modifier
            .padding(10.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = modifier
                .padding(12.dp)
                .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        ) {
            Row {
                Text(
                    text = tile.it.name,
                    modifier = Modifier.weight(3f)
                )

                IconButton(
                    onClick = {expanded = !expanded},
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = null
                    )
                }

            }

            Text(text = "completed on: " + dateFormatter(tile.completionDate))

            Spacer(Modifier.padding(2.dp))

            Text(text = "started on: " + dateFormatter(tile.it.dateOfCreation))

            Spacer(Modifier.padding(2.dp))

            if (type.hasPriority()) {
                Text("priority: " + (tile.it as PriorityTask).priority)
                Spacer(Modifier.padding(2.dp))
            }

            if (type.hasDeadline()) {
                Text("deadline: " + dateFormatter((tile.it as DeadlineTask).deadline))
                Spacer(Modifier.padding(2.dp))
            }

            if (type.hasCategory()) {
                Text("deadline: " + (tile.it as CategoryTask).category)
                Spacer(Modifier.padding(2.dp))
            }

            if (expanded) {
                Text("description: " + tile.it.description)
            }

        }

    }
}

@Preview(showBackground = true)
@Composable
fun HistoryListPreview(){
    val viewModel: StateHolder = viewModel(factory = AppViewModelProvider.Factory)
    HistoryList(viewModel, viewModel.Read.getHistoryList(), type = viewModel.Read.getCurrentType())
}
