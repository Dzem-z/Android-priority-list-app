package com.example.prioritylist.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.prioritylist.data.backend.Task
import com.example.prioritylist.data.backend.TaskTypes
import java.time.LocalDateTime
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.DatePicker
import androidx.compose.material3.Text as Material3Text
import androidx.compose.material3.TextButton as Material3TextButton
import androidx.compose.material3.DatePickerDialog
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewmodel.compose.viewModel
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.Calendar
import java.util.TimeZone


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskScreen(
    holder: StateHolder = viewModel(factory = AppViewModelProvider.Factory),
    onConfirmMessage: String,
    modifier: Modifier = Modifier,
    onConfirm: () -> Unit = {},
    onCancel: () -> Unit = {}
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

        val focusRequester = remember { FocusRequester() }

        val focusManager = LocalFocusManager.current

        var nameTextFieldValueState by remember {
            mutableStateOf(
                TextFieldValue(
                    text = holder.UI.editedTask.name,
                    selection = TextRange(holder.UI.editedTask.name.length)
                )
            )
        }

        var descriptionTextFieldValueState by remember {
            mutableStateOf(
                TextFieldValue(
                    text = holder.UI.editedTask.description,
                    selection = TextRange(holder.UI.editedTask.description.length)
                )
            )
        }

        var priorityTextFieldValueState by remember {
            mutableStateOf(
                TextFieldValue(
                    text = if (holder.UI.editedTask.priority.toString() == "0") "" else holder.UI.editedTask.priority.toString(),
                    selection = TextRange(holder.UI.editedTask.name.length)
                )
            )
        }

        val openDialog = remember { mutableStateOf(false) }

        Column(
            Modifier
                .padding(innerPadding)
                .padding(24.dp)) {
            OutlinedTextField(
                value = nameTextFieldValueState,
                singleLine = true,
                label = { Text(text = "task name") },
                isError = holder.UI.isDuplicatedTask(),
                trailingIcon = {
                    if (holder.UI.isDuplicatedTask())
                        Icon(Icons.Outlined.Error, "error", tint = MaterialTheme.colors.error)
                },
                onValueChange = {
                    nameTextFieldValueState = it
                    holder.UI.updateNameOfEditedTask(it.text)
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                modifier = Modifier.focusRequester(focusRequester)
            )

            if (holder.UI.duplicatedName) {
                Text(
                    text = "task with given name already in list",
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            if (holder.UI.emptyName) {
                Text(
                    text = "task name can't be empty",
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Spacer(
                Modifier.padding(12.dp)
            )

            TextField(
                value = descriptionTextFieldValueState,
                label = { Text(text = "description") },
                onValueChange = {
                    descriptionTextFieldValueState = it
                    holder.UI.updateDescriptionOfEditedTask(it.text)
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = if (holder.Read.getCurrentType().hasPriority() || holder.Read.getCurrentType().hasCategory() || holder.Read.getCurrentType().hasDeadline())
                         ImeAction.Next
                    else
                        ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        if(holder.Read.getCurrentType().hasDeadline() && !holder.Read.getCurrentType().hasPriority())
                            openDialog.value = true
                        focusManager.moveFocus(FocusDirection.Down)
                             },
                    onDone = { onConfirm() }
                )
            )

            Spacer(
                Modifier.padding(12.dp)
            )

            if (holder.Read.getCurrentType().hasPriority()) {
                TextField(
                    value = priorityTextFieldValueState,
                    label = { Text(text = "priority") },
                    onValueChange = {
                        priorityTextFieldValueState = it
                        holder.UI.updatePriorityOfEditedTask(it.text)
                                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = if (holder.Read.getCurrentType().hasCategory() || holder.Read.getCurrentType().hasDeadline())
                            ImeAction.Next
                        else
                            ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            if(holder.Read.getCurrentType().hasDeadline())
                                openDialog.value = true
                            focusManager.moveFocus(FocusDirection.Down)
                                 },
                        onDone = { onConfirm() }
                    )
                )

                Spacer(
                    Modifier.padding(12.dp)
                )
            }

            if (holder.Read.getCurrentType().hasCategory()) {

            }

            if (holder.Read.getCurrentType().hasDeadline()){

                var timePointer = remember { mutableStateOf(Calendar.getInstance()) }
                val datePickerState = rememberDatePickerState(
                    initialSelectedDateMillis = Calendar.getInstance().time.toInstant().toEpochMilli())
                if (openDialog.value) {

                    val confirmEnabled = derivedStateOf { datePickerState.selectedDateMillis != null}
                    DatePickerDialog(
                        onDismissRequest = {
                            openDialog.value = false
                        },
                        confirmButton = {
                            Material3TextButton(
                                onClick = {
                                    timePointer.value.setTimeInMillis(datePickerState.selectedDateMillis!!)
                                    holder.UI.editedTask.deadline = timePointer.value.time
                                    openDialog.value = false

                                },
                                enabled = confirmEnabled.value
                            ) {
                                Material3Text("OK")
                            }

                        },
                        dismissButton = {
                            Material3TextButton(
                                onClick = {
                                    openDialog.value = false
                                }
                            ) {
                                Material3Text("Cancel")
                            }

                        }
                    ) {
                        DatePicker(state = datePickerState)
                    }
                }
                OutlinedButton(onClick =  {
                    openDialog.value = true
                }
                ){
                    Row{
                        Text(text = timePointer.value.get(Calendar.DAY_OF_MONTH).toString())
                        Text(text = "select deadline")
                    }

                }
                Spacer(
                    Modifier.padding(12.dp)
                )

            }

            Row(

            ) {

                OutlinedButton(
                    onClick = {
                        onCancel()
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

            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }

        }


    }
}


@Composable
@Preview(showBackground = true)
fun EditTaskScreenPreview() {
    EditTaskScreen(
        onConfirmMessage = "add Task"
    )
}