package com.example.prioritylist.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prioritylist.R
import com.example.prioritylist.data.backend.TaskTypes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
* [TaskOptionsSheet] is a composable function that displays task options visible in bottomSheetLayout
* @param hide: a lambda called when dismiss behaviour is triggered
* @param onEditTask: a lambda called when user clicks edit button (should display editTaskScreen)
* @param onDeleteTask: a lambda called when user clicks delete button (shoulde delete selected task)
* */

@Composable
fun TaskOptionsSheet(
    modifier: Modifier = Modifier,
    hide: () -> Unit = {},
    onEditTask: () -> Unit = {},
    onDeleteTask: () -> Unit = {}
) {
    Row(
        modifier = Modifier.height(96.dp)
    ){
        Button(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(12.dp),
            onClick = {
                hide()
                onEditTask()
            }
        ) {
            Text(text = "edit task")
        }

        Spacer(modifier = Modifier.padding(6.dp))

        Button(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(12.dp),
            onClick = {
                hide()
                onDeleteTask()
            }
        ) {
            Text(text = "delete task")
        }
    }
}

/**
 * [ListOptionsSheet] is a composable used for displaying list options in a BottomSheetLayout
 * @param globalScope is a global coroutineScope used for launching methods that save data to database
 * @param viewModel is the viewModel responsible for data and state management
 * @param removeList is a lambda that removes current list
 * @param addList is a lambda that triggers the addList screen
 * @param goToHistory is a lambda that triggers the historyList screen
 * @param launchSnackbar launches a snackbar saying that swaping of two list ended succesfully
 */

@Composable
fun ListOptionsSheet(
    globalScope: CoroutineScope = rememberCoroutineScope(),
    viewModel: StateHolder = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier,
    removeList: () -> Unit = {},
    addList: () -> Unit = {},
    goToHistory: () -> Unit = {},
    launchSnackbar: (String) -> Unit = {}
) {

    //state variable that points if name of current list is edited or not
    var isEdited by remember{ mutableStateOf(false) }

    //a focusRequester used for requesting focus on name field when user selects edit button
    val focusRequester = remember { FocusRequester() }


    var textFieldValueState by remember {
        mutableStateOf(
            TextFieldValue(
                text = viewModel.UI.currentListName
            )
        )
    }

    Column(
        modifier = Modifier
            .height(258.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 0.dp)
        ){

            Spacer(Modifier.weight(1f))

            if (isEdited){
                OutlinedTextField(
                    value = textFieldValueState,
                    modifier = Modifier.focusRequester(focusRequester),
                    label = {},
                    onValueChange = {
                        textFieldValueState = it
                        viewModel.UI.currentListName = it.text
                        viewModel.setName()
                    },
                    textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            viewModel.setName()
                            isEdited = !isEdited
                        }
                    ),
                )
                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()  //requests focus when launched
                }
            } else {
                Text(
                    text = viewModel.UI.currentListName,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(3f)

                )
            }
            IconButton( //edit button
                onClick = {
                    if(isEdited) {
                    viewModel.setName()
                    }
                    textFieldValueState = TextFieldValue(
                        text = viewModel.UI.currentListName,
                        selection = TextRange(viewModel.UI.currentListName.length)
                    )
                    isEdited = !isEdited
                          },
                modifier = Modifier
                    .weight(1f)

            ){
                Icon(
                    imageVector = if(isEdited) Icons.Outlined.Done else Icons.Outlined.Edit,
                    contentDescription = "Drawer Icon"
                )
            }
        }
        Text(
            text = viewModel.Read.getDateOfCreation().toString(),
            textAlign = TextAlign.Center
        )
        Text(
            text = when(viewModel.Read.getCurrentType()){
                TaskTypes.DEADLINE -> "deadline-based tasks"
                TaskTypes.PRIORITY -> "priority-based tasks"
                TaskTypes.DEADLINE_PRIORITY_CATEGORY -> "deadline-priority-category-based tasks"
                TaskTypes.DEADLINE_PRIORITY -> "deadline-priority-based tasks"
                TaskTypes.DEADLINE_CATEGORY -> "deadline-category-based tasks"
                TaskTypes.CATEGORY -> "category based tasks"
            },
            textAlign = TextAlign.Center
        )

        Row(
            modifier = Modifier.padding(12.dp)
        ) {

            Button(
                onClick = {
                    globalScope.launch {
                        viewModel.swapWithLeft()    //swaps current list with list on the left
                    }
                    launchSnackbar("swaped with list on the left")
                          },
                enabled = viewModel.Read.isPrevList,
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.swap_horiz_48px),
                        contentDescription = "drawer icon"
                    )
                    Text(text = "swap with left")
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(2f)
            ) {
                OutlinedButton(
                    onClick = removeList
                ) {
                    Text(text = "remove this list")
                }

                OutlinedButton(
                    onClick = addList
                ) {
                    Text(text = "add new list")
                }

                Button(
                    onClick = goToHistory
                ) {
                    Text(text = "go to history")
                }
            }

            Button(
                onClick = {
                    globalScope.launch {
                        viewModel.swapWithRight() //swaps current list with list on the left
                    }
                    launchSnackbar("swaped with list on the right")
                          },
                enabled = viewModel.Read.isNextList,
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.swap_horiz_48px),
                        contentDescription = "drawer icon"
                    )

                    Text(text = "swap with right")
                }
            }

        }

    }
}

@Preview(showBackground = true)
@Composable
fun TaskOptionsSheetPreview() {
    TaskOptionsSheet()
}

@Preview(showBackground = true)
@Composable
fun ListOptionsSheetPreview() {
    ListOptionsSheet()
}