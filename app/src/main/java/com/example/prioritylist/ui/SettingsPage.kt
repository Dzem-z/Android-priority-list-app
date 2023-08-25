package com.example.prioritylist.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prioritylist.R
import com.example.prioritylist.data.database.Converters
import com.example.prioritylist.ui.theme.PriorityListTheme
import com.example.prioritylist.ui.theme.ThemeID
import kotlinx.coroutines.flow.toSet
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(
   modifier: Modifier = Modifier,
   holder: StateHolder = viewModel(factory = AppViewModelProvider.Factory),
   displaySidebar: () -> Unit = {}
) {

   val theme = holder.Settings.themeIdStateFlow.collectAsState()

   var deadlinePeriodTextFieldValueState by remember {
      mutableStateOf(
         TextFieldValue(
            text = "",
            //0 represented as empty string
         )
      )
   }

   var isExpandedMenu by remember { mutableStateOf(false) }

   Scaffold(
      topBar = {
         TopAppBar(
            backgroundColor = MaterialTheme.colorScheme.onPrimary,
            title = { /*TODO*/ },
            navigationIcon = {
               IconButton(
                  onClick = { displaySidebar() }
               ) {
                  Icon(
                     imageVector = Icons.Rounded.Menu,
                     contentDescription = "Drawer Icon"
                  )
               }
            },
            actions = {
               /*TODO*/
            }
         )


      }
   ) { innerPadding ->
      Column(modifier = Modifier.padding(innerPadding)){

         Text(
            text = stringResource(id = R.string.theme_settings),
            modifier = Modifier.padding(12.dp)
         )

         //Spacer(modifier = Modifier.padding(6.dp))

         Row(
            modifier = Modifier
               .fillMaxWidth()
               .padding(12.dp)
         ) {
            Text(
               modifier = modifier
                  .fillMaxWidth()
                  .weight(1f)
                  .padding(vertical = 14.dp)
               ,
               textAlign = TextAlign.Center,
               text = "select theme:"
            )

            Spacer(modifier = Modifier.padding(12.dp))

            ExposedDropdownMenuBox(
               expanded = isExpandedMenu,
               onExpandedChange = {
                  isExpandedMenu = !isExpandedMenu
               },
               modifier = Modifier
                  .fillMaxWidth()
                  .weight(2f)
            ) {
               TextField(
                  value = theme.value.themeName,
                  singleLine = true,
                  onValueChange = {},
                  readOnly = true,
                  trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpandedMenu) },
                  modifier = Modifier.menuAnchor()
               )

               ExposedDropdownMenu(
                  expanded = isExpandedMenu,
                  onDismissRequest = { isExpandedMenu = false }
               ) {
                  ThemeID.values().forEach { item ->
                     DropdownMenuItem(
                        text = {
                           Text(text = item.themeName)
                        },
                        onClick = {
                           holder.Settings.saveThemeId(item)
                        }
                     )
                  }
               }
            }
         }

         Spacer(modifier = Modifier.padding(16.dp))

         Text(text = "deadline period: " + holder.Settings.deadlinePeriodDays.collectAsState().value.toString() + " days")

         TextField(
            value =  deadlinePeriodTextFieldValueState,
            label = { Text(text = "days") },
            isError = holder.UI.priorityOverflowError,
            trailingIcon = {
               if (holder.UI.priorityOverflowError)
                  Icon(Icons.Outlined.Error, stringResource(id = R.string.error), tint = MaterialTheme.colorScheme.error)
            },
            onValueChange = { new ->
               if (!holder.UI.checkForOverflowError(new.text)) {
                  deadlinePeriodTextFieldValueState = new
                  holder.Settings.saveDeadlinePeriodDays(deadlinePeriodTextFieldValueState.text.let { if (it == "") "0" else it }
                     .toUInt())
               }
            }
         )

         if (holder.UI.priorityOverflowError) {//error messages
            Text(
               text = stringResource(id = R.string.overflow_error_3),
               color = MaterialTheme.colorScheme.error,
               //style = MaterialTheme.typography.caption,
               modifier = Modifier.padding(start = 16.dp)
            )
         }

      }
   }
}

@Preview(showBackground = true)
@Composable
fun SettingsPagePreview(){
   val viewModel: StateHolder = viewModel(factory = AppViewModelProvider.Factory)
   val theme = viewModel.Settings.themeIdStateFlow.collectAsState()

   PriorityListTheme(theme.value) {  //initializes apps theme with proper theme according to ThemeID val read from dataStore object
      // A surface container using the 'background' color from the theme
      Surface(
         modifier = Modifier.fillMaxSize(),
         color = MaterialTheme.colorScheme.background
      ) {
         SettingsPage(holder = viewModel)
      }
   }
}