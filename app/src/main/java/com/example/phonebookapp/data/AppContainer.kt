package com.example.phonebookapp.data

import android.content.Context

interface AppContainer {
    val itemsRepository: ItemsRepository
}

class AppDataContainer(private val context: Context): AppContainer{
    override val itemsRepository: ItemsRepository by lazy{
        OfflineItemsRepository(PeopleDatabase.getDatabase(context).itemDao())
    }
}