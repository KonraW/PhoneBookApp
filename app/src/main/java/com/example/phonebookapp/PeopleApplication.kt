package com.example.phonebookapp

import android.app.Application
import com.example.phonebookapp.data.AppContainer
import com.example.phonebookapp.data.AppDataContainer

class PeopleApplication: Application(){
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}