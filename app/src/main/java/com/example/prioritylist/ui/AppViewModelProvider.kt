package com.example.prioritylist.ui

import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import com.example.prioritylist.PriorityListApplication

/**
 * Provides Factory to create instance of ViewModel for the entire PriorityList app
 */

object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for StateHolder
        initializer {
            StateHolder(
                priorityListApplication().container.listRepository,
                priorityListApplication().container.mainRepository
            )
        }
    }
}

fun CreationExtras.priorityListApplication(): PriorityListApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as PriorityListApplication)