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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.prioritylist.R
import com.example.prioritylist.data.TaskTypes


@Composable
fun TaskOptionsSheet(modifier: Modifier = Modifier, hide: () -> Unit = {}, onEditTask: () -> Unit = {}, onDeleteTask: () -> Unit = {}) {
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

@Composable
fun ListOptionsSheet(viewModel: StateHolder, modifier: Modifier = Modifier, removeList: () -> Unit = {}, addList: () -> Unit = {}, goToHistory: () -> Unit = {}, launchSnackbar: (String) -> Unit = {}) {

    var isEdited by remember{ mutableStateOf(false) }

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
                    focusRequester.requestFocus()
                }
            } else {
                Text(
                    text = viewModel.UI.currentListName,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(3f)

                )
            }
            IconButton(
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
                    viewModel.swapWithLeft()
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
                    viewModel.swapWithRight()
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
    ListOptionsSheet(StateHolder())
}