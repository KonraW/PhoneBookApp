package com.example.phonebookapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.phonebookapp.ui.theme.PhoneBookAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PhoneBookAppTheme {
                // A surface container using the 'background' color from the theme
                PhoneBookApp()
            }
        }
    }


}

