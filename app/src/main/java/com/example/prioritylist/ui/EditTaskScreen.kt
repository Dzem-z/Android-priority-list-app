package com.example.prioritylist.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentComposer
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.prioritylist.data.Task
import com.example.prioritylist.data.TaskTypes
import java.time.LocalDateTime

@Composable
fun EditTaskScreen(
    holder: StateHolder,
    onConfirmMessage: String,
    modifier: Modifier = Modifier,
    onConfirm: () -> Unit = {}
) {

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
                }
            )


        }

    ) { innerPadding ->

        Column(
            Modifier
                .padding(innerPadding)
                .padding(24.dp)) {
            OutlinedTextField(
                value = holder.editedTask.name,
                singleLine = true,
                label = { Text(text = "task name") },
                onValueChange = {
                    holder.updateNameOfEditedTask(it)
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { /*TODO*/ }
                )
            )

            Spacer(
                Modifier.padding(12.dp)
            )

            TextField(
                value = holder.editedTask.description,
                label = { Text(text = "description") },
                onValueChange = {
                    holder.updateDescriptionOfEditedTask(it)
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {  }
                )
            )

            Spacer(
                Modifier.padding(12.dp)
            )

            if (holder.getType() == TaskTypes.PRIORITY) {
                TextField(
                    value = holder.editedTask.priority.toString(),
                    label = { Text(text = "priority") },
                    onValueChange = { holder.updatePriorityOfEditedTask(it) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {  }
                    )
                )

                Spacer(
                    Modifier.padding(12.dp)
                )
            }

            Row(

            ) {

                OutlinedButton(
                    onClick = {
                        /*TODO*/
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "drawer icon"
                    )

                    Spacer(Modifier.padding(5.dp))

                    Text(text = "Cancel")

                }

                Spacer(Modifier.weight(0.5f))

                Button(
                    onClick = onConfirm,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Done,
                        contentDescription = "drawer icon"
                    )

                    Spacer(Modifier.padding(5.dp))

                    Text(text = onConfirmMessage)
                }
            }


        }
    }
}

@Composable
@Preview(showBackground = true)
fun EditTaskScreenPreview() {
    EditTaskScreen(
        holder = StateHolder(),
        onConfirmMessage = "add Task"
    )
}