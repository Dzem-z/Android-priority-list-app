package com.example.prioritylist.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Button
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.OutlinedButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.prioritylist.data.TaskTypes
import com.example.prioritylist.data.listOfTypes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddListScreen(
    UIholder: StateHolder.UiViewModel = StateHolder().UI,
    onCancel: () -> Unit = {},
    onConfirm: () ->Unit = {}
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

        var expanded by remember { mutableStateOf(false) }

        Column(
            Modifier
                .padding(innerPadding)
                .padding(24.dp)
        ) {
            OutlinedTextField(
                value = UIholder.newListName,
                singleLine = true,
                label = { Text(text = "list name") },
                onValueChange = {
                    UIholder.newListName = it
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                modifier = Modifier.focusRequester(focusRequester)
            )

            Spacer(
                Modifier.padding(12.dp)
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                TextField(
                    value = UIholder.selectedTypeText,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    listOfTypes.forEach { item ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = when(item){
                                        TaskTypes.DEADLINE -> "deadline-based tasks"
                                        TaskTypes.PRIORITY -> "priority-based tasks"
                                        TaskTypes.DEADLINE_PRIORITY_CATEGORY -> "deadline-priority-category-based tasks"
                                        TaskTypes.DEADLINE_PRIORITY -> "deadline-priority-based tasks"
                                        TaskTypes.DEADLINE_CATEGORY -> "deadline-category-based tasks"
                                        TaskTypes.CATEGORY -> "category based tasks"
                                    }
                                )
                            },
                            onClick = {
                                UIholder.selectedTypeText = when (item) {
                                    TaskTypes.DEADLINE -> "deadline-based tasks"
                                    TaskTypes.PRIORITY -> "priority-based tasks"
                                    TaskTypes.DEADLINE_PRIORITY_CATEGORY -> "deadline-priority-category-based tasks"
                                    TaskTypes.DEADLINE_PRIORITY -> "deadline-priority-based tasks"
                                    TaskTypes.DEADLINE_CATEGORY -> "deadline-category-based tasks"
                                    TaskTypes.CATEGORY -> "category based tasks"
                                }

                                UIholder.selectedType = item

                                expanded = false

                            }
                        )
                    }
                }
            }

            Spacer(Modifier.padding(12.dp))

            Row(

            ) {
                OutlinedButton(
                    onClick = onCancel,
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

                    Text(text = "add list")
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun AddListScreenPreview() {
    AddListScreen()
}