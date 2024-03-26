package com.example.phonebookapp.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.phonebookapp.PeopleApplication
import com.example.phonebookapp.ui.home.HomeViewModel

object AppViewModelProvider {
    val Factory= viewModelFactory {
        initializer {
            HomeViewModel(peopleApplication().container.itemsRepository)
        }
    }
}

fun CreationExtras.peopleApplication(): PeopleApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as PeopleApplication)