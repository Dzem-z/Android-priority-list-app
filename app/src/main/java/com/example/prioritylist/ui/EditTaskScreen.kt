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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prioritylist.R
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.Calendar
import java.util.TimeZone


/**
* [EditTaskScreen] is a composable that displays edit or add screen to the user
* @param holder is an instance of StateHolder initialized by viewModel factory
* @param onConfirmMessage is an message displaying on confirm button
* @onConfirm is called when user confirms action
* @onCancel is called when user cancels action
* */

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

        val focusRequester = remember { FocusRequester() }  //focusRequester initially requests focus

        val focusManager = LocalFocusManager.current

        //used for initially setting up pointer at the end of edited word
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

        val openDialog = remember { mutableStateOf(false) } //if type dialog is opened

        Column(
            Modifier
                .padding(innerPadding)
                .padding(24.dp)) {
            OutlinedTextField(
                value = nameTextFieldValueState,
                singleLine = true,
                label = { Text(text = stringResource(id = R.string.task_name)) },
                isError = holder.UI.isDuplicatedTask(),
                trailingIcon = {
                    if (holder.UI.isDuplicatedTask())
                        Icon(Icons.Outlined.Error, stringResource(id = R.string.error), tint = MaterialTheme.colors.error)
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

            if (holder.UI.duplicatedName) {//error messages
                Text(
                    text = stringResource(id = R.string.duplicated_task_error),
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            if (holder.UI.emptyName) {//error messages
                Text(
                    text = stringResource(id = R.string.empty_task_name_error),
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
                label = { Text(text = stringResource(id = R.string.task_description)) },
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

            if (holder.Read.getCurrentType().hasPriority()) {   //if current task has priority atributte
                TextField(
                    value = priorityTextFieldValueState,
                    label = { Text(text = stringResource(id = R.string.task_priority)) },
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

            if (holder.Read.getCurrentType().hasCategory()) { //if current task has category atributte

            }

            if (holder.Read.getCurrentType().hasDeadline()){ //if current task has deadline atributte

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
                                Material3Text(stringResource(id = R.string.OK))
                            }

                        },
                        dismissButton = {
                            Material3TextButton(
                                onClick = {
                                    openDialog.value = false
                                }
                            ) {
                                Material3Text(stringResource(id = R.string.cancel))
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
                        Text(text = stringResource(id = R.string.selected_deadline))
                        Text(text = dateFormatter(timePointer.value.time))
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

                    Text(text = stringResource(R.string.cancel))

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

            LaunchedEffect(Unit) {//request focus for name textfield at startup
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