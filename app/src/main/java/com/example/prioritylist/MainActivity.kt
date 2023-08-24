package com.example.prioritylist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prioritylist.ui.AppViewModelProvider
import com.example.prioritylist.ui.MainPageScreen
import com.example.prioritylist.ui.StateHolder
import com.example.prioritylist.ui.theme.PriorityListTheme
import com.example.prioritylist.ui.theme.ThemeID

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: StateHolder = viewModel(factory = AppViewModelProvider.Factory)
            val theme = viewModel.Settings.themeIdStateFlow.collectAsState()

            PriorityListTheme(theme.value) {  //initializes apps theme with proper theme according to ThemeID val read from dataStore object
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainPageScreen(holder = viewModel)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PriorityListTheme(ThemeID.FIRST) {
        MainPageScreen()
    }
}