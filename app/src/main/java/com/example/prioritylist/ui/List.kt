package com.example.prioritylist.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.prioritylist.data.DeadlineTask
import com.example.prioritylist.data.PriorityTask
import com.example.prioritylist.data.Task
import com.example.prioritylist.data.TaskTypes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun PriorityList(
    viewModel: StateHolder,
    list: MutableList<PriorityTask>,
    onLongPress: () -> Unit,
    modifier: Modifier = Modifier
) {

        LazyColumn(
            modifier = Modifier
                .padding(end = 10.dp)
                .fillMaxHeight()
        ){
            items(
                list,
                key = { it.name }
            ) {item->
                PriorityTaskTile(
                    tile = item as PriorityTask,
                    modifier = Modifier
                        .animateItemPlacement()
                        .pointerInput(item) {
                        detectTapGestures(
                            onPress = { /* Called when the gesture starts */ },
                            onDoubleTap = { viewModel.onDone(item) },
                            onLongPress = {
                                viewModel.resetEditedTask()
                                viewModel.updateDescriptionOfEditedTask(item.description)
                                viewModel.updateNameOfEditedTask(item.name)
                                viewModel.editedTask.id = item.id
                                viewModel.editedTask.dateOfCreation = item.dateOfCreation
                                viewModel.updatePriorityOfEditedTask(item.priority.toString())
                                onLongPress()
                                          },
                            onTap = { /* Called on Tap */ }
                        )
                    }

                )
            }
        }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun DeadlineList(
    viewModel: StateHolder,
    list: MutableList<DeadlineTask>,
    onLongPress: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = Modifier
            .padding(end = 10.dp)
            .fillMaxHeight()
    ){
        items(
            list,
            key = { it.name }
        ) {item->
            DeadlineTaskTile(
                tile = item as DeadlineTask,
                modifier = Modifier
                    .animateItemPlacement()
                    .pointerInput(item) {
                        detectTapGestures(
                            onPress = { /* Called when the gesture starts */ },
                            onDoubleTap = { viewModel.onDone(item) },
                            onLongPress = {
                                viewModel.resetEditedTask()
                                viewModel.updateDescriptionOfEditedTask(item.description)
                                viewModel.updateNameOfEditedTask(item.name)
                                viewModel.editedTask.dateOfCreation = item.dateOfCreation
                                viewModel.editedTask.id = item.id
                                viewModel.editedTask.deadline = item.deadline
                                onLongPress()
                                          },
                            onTap = { /* Called on Tap */ }
                        )
                    }

            )
        }
    }
}

@Composable
fun PriorityTaskTile(tile: PriorityTask, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        elevation = 10.dp,
        modifier = modifier
            .padding(10.dp)
            .fillMaxWidth()

    ){
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
                    text = tile.name,
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

            Row {
                Text(text = "priority: " + tile.priority)
            }

            if(expanded){

                Spacer(modifier = Modifier.padding(6.dp))

                Text(text = "description: " + tile.description)
            }

        }
    }
}

@Composable
fun DeadlineTaskTile(tile: DeadlineTask, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        elevation = 10.dp,
        modifier = modifier
            .padding(10.dp)
            .fillMaxWidth()
    ){
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
                    text = tile.name,
                    modifier = Modifier.weight(3f)
                )

                IconButton(
                    onClick = { expanded = !expanded },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = null
                    )
                }
            }
            Row{
                Text(text = "deadline: " + tile.deadline)
            }

            if(expanded){

                Spacer(modifier = Modifier.padding(6.dp))

                Text(text = "description: " + tile.description)
            }

        }
    }
}
/*
@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview
fun ListPreview() {

    val holder = StateHolder()

    PriorityList(
        holder,
        mutableListOf<PriorityTask>()
        //holder.displayingList as MutableList<PriorityTask>
    )
}*/