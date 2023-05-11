package com.example.prioritylist.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.prioritylist.data.CategoryTask
import com.example.prioritylist.data.DeadlinePriorityTask
import com.example.prioritylist.data.DeadlineTask
import com.example.prioritylist.data.ModifiableTask
import com.example.prioritylist.data.PriorityTask
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

    Column(Modifier.padding(12.dp)) {
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
            label = { Text(text = "description")},
            onValueChange = {
                holder.updateDescriptionOfEditedTask(it)
            }
        )
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