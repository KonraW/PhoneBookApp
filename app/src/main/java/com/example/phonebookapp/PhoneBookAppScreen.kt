package com.example.phonebookapp

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.example.phonebookapp.ui.home.HomeScreen


@Composable
fun NotebookAppScreen() {
    Scaffold {innerPadding ->
        HomeScreen(name = "Android")
    }
}